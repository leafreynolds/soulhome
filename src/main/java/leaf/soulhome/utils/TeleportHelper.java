/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome.utils;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.network.play.server.SPlayerAbilitiesPacket;
import net.minecraft.network.play.server.SServerDifficultyPacket;
import net.minecraft.network.play.server.SSetExperiencePacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraft.world.storage.IWorldInfo;

public class TeleportHelper
{
    public static void teleportEntity(Entity entity, ServerWorld destinationDimension, double x, double y, double z, float yRot, float xRot)
    {
        if (entity == null || entity.level.isClientSide || !entity.canChangeDimensions())
        {
            return;
        }

        ServerWorld currentDimension = entity.getServer().getLevel(entity.getCommandSenderWorld().dimension());


        boolean isChangingDimension = !currentDimension.dimension().location().equals(destinationDimension.dimension().location());
        final boolean entityIsPlayer = entity instanceof ServerPlayerEntity;
        final ServerPlayerEntity serverPlayerEntity = entityIsPlayer ? (ServerPlayerEntity) entity : null;

        if (isChangingDimension && !entity.canChangeDimensions())
        {
            //early exit
            return;
        }

        //no passengers allowed.
        if (entity.isPassenger())
        {
            entity.getRootVehicle().unRide();
        }


        //This section is mostly copied and then modified from TeleportCommand.performTeleport()
        if (entityIsPlayer)
        {
            ChunkPos chunkPos = new ChunkPos(new BlockPos(x, y, z));
            destinationDimension.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, chunkPos, 1, entity.getId());
            entity.stopRiding();
            if (serverPlayerEntity.isSleeping())
            {
                serverPlayerEntity.stopSleepInBed(true, true);
            }

            if (isChangingDimension)
            {
                serverPlayerEntity.teleportTo(destinationDimension, x, y, z, yRot, xRot);
            }
            else
            {
                serverPlayerEntity.connection.teleport(x, y, z, yRot, xRot);
            }

            serverPlayerEntity.setYHeadRot(yRot);

            //restore stuff. Annoyingly it doesn't happen automatically
            for (EffectInstance effectinstance : serverPlayerEntity.getActiveEffects())
            {
                serverPlayerEntity.connection.send(new SPlayEntityEffectPacket(serverPlayerEntity.getId(), effectinstance));
            }

            IWorldInfo worldInfo = serverPlayerEntity.level.getLevelData();
            //I'd always wondered what the deal was with xp not showing properly.
            serverPlayerEntity.connection.send(new SPlayerAbilitiesPacket(serverPlayerEntity.abilities));
            serverPlayerEntity.connection.send(new SServerDifficultyPacket(worldInfo.getDifficulty(), worldInfo.isDifficultyLocked()));
            serverPlayerEntity.connection.send(new SSetExperiencePacket(serverPlayerEntity.experienceProgress, serverPlayerEntity.totalExperience, serverPlayerEntity.experienceLevel));
        }
        else
        {
            float entityYRot = MathHelper.wrapDegrees(yRot);
            float entityXRot = MathHelper.wrapDegrees(xRot);
            entityXRot = MathHelper.clamp(entityXRot, -90.0F, 90.0F);
            if (isChangingDimension)
            {
                Entity originalEntity = entity;
                entity = originalEntity.getType().create(destinationDimension);
                if (entity == null)
                {
                    //error
                    LogHelper.error("Was unable to create an entity when trying to teleport it.");
                    return;
                }
                entity.restoreFrom(originalEntity);
                entity.moveTo(x, y, z, entityYRot, entityXRot);
                entity.setYHeadRot(entityYRot);

                currentDimension.despawn(originalEntity);

                destinationDimension.addFromAnotherDimension(entity);

                currentDimension.resetEmptyTime();
                destinationDimension.resetEmptyTime();
            }
            else
            {
                entity.moveTo(x, y, z, entityYRot, entityXRot);
                entity.setYHeadRot(entityYRot);
            }
        }

        //not sure if I care about elytra, SO todo decide later. might be fun to let them keep flying?
        if (!(entity instanceof LivingEntity) || !((LivingEntity) entity).isFallFlying())
        {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
            entity.setOnGround(true);
        }

        if (entity instanceof CreatureEntity)
        {
            ((CreatureEntity) entity).getNavigation().stop();
        }
    }
}
