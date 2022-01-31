/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome.utils;

import leaf.soulhome.SoulHome;
import leaf.soulhome.registry.DimensionRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Registry;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.List;

import static leaf.soulhome.constants.Constants.NBTKeys.*;

public class DimensionHelper
{
    public static final int FLOOR_LEVEL = 64;

    // Check if the user is in the soul dimension
    // used for deciding if we are moving the player in or out of the dimension.
    public static boolean isInSoulDimension(LivingEntity livingEntity)
    {
        //done by comparing the user's dimension to the soul dimension type.
        return isDimensionOfType(livingEntity.getCommandSenderWorld(), DimensionRegistry.DimensionTypes.SOUL_DIMENSION_TYPE);
    }

    public static boolean isDimensionOfType(Level world, ResourceKey<DimensionType> dimTypeKey)
    {
        // I think it has to be done this way since the soul dimension is not
        // shared between players like it is in random things SpectreKey dimension
        DimensionType type = world.registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).get(dimTypeKey);
        return type != null && type.equalTo(world.dimensionType());
    }

    // move the player to/from their soul dimension
    public static void FlipDimension(Player playerEntity, MinecraftServer server, List<Entity> entitiesInRange)
    {
        //get (or create if this is the first time) our little save
        CompoundTag soulNBT = PlayerHelper.getPersistentTag(playerEntity, SoulHome.SOULHOME_LOC.toString());
        ServerLevel destination;
        double x = 0.5d;
        double y = FLOOR_LEVEL + 2;
        double z = 0.5d;

        if (entitiesInRange == null)
        {
            entitiesInRange = new ArrayList<>();
            entitiesInRange.add(playerEntity);
        }

        // move to the last saved place before player went to soul dimension
        if (isInSoulDimension(playerEntity))
        {
            //get the dimension key, based on the info we saved.
            ResourceKey<Level> destinationKey =
                    ResourceKey.create(
                            Registry.DIMENSION_REGISTRY,
                            new ResourceLocation(
                                    soulNBT.getString(LAST_DIMENSION_MOD_ID),
                                    soulNBT.getString(LAST_DIMENSION_MOD_DIMENSION))
                    );

            //positioning
            x = soulNBT.getDouble(LAST_DIMENSION_X);
            y = soulNBT.getDouble(LAST_DIMENSION_Y);
            z = soulNBT.getDouble(LAST_DIMENSION_Z);

            //then get the destination dimension by using that key.
            try
            {
                destination = server.getLevel(destinationKey);
            }
            catch (Exception e)//sometimes people remove mods. Protect against unknown by sending them to overworld spawn.
            {
                destination = server.overworld();

                final BlockPos sharedSpawnPos = destination.getSharedSpawnPos();
                x = sharedSpawnPos.getX();
                y = sharedSpawnPos.getY();
                z = sharedSpawnPos.getZ();
            }
        }
        // not in the soul, lets go there
        else
        {
            //now we can go to the soul
            //will create the dimension for that user if it's the first time accessing it
            destination = getOrCreateSoulDimension(playerEntity.getStringUUID(), server);

        }

        //dimension location eg minecraft:overworld
        ResourceLocation location = playerEntity.getCommandSenderWorld().dimension().location();

        //then teleport everyone
        for (Entity ent : entitiesInRange)
        {
            //if it's a player entity, save their last dimension position individually.
            //helps them leave another player's soul. Nothing else can leave without help.
            if (ent instanceof Player && !isInSoulDimension((Player) ent))
            {
                soulNBT = PlayerHelper.getPersistentTag((Player) ent, SoulHome.SOULHOME_LOC.toString());
                // XYZ
                soulNBT.putDouble(LAST_DIMENSION_X, ent.getX());
                soulNBT.putDouble(LAST_DIMENSION_Y, ent.getY());
                soulNBT.putDouble(LAST_DIMENSION_Z, ent.getZ());

                // save the dimension info
                soulNBT.putString(LAST_DIMENSION_MOD_ID, location.getNamespace());
                soulNBT.putString(LAST_DIMENSION_MOD_DIMENSION, location.getPath());

            }


            Vec3 posRelativeToTeleporter = ent.position().subtract(playerEntity.position());
            Vec3 newPosByDestination = new Vec3(x,y,z).add(posRelativeToTeleporter);


            TeleportHelper.teleportEntity(
                    ent,
                    destination,
                    newPosByDestination.x,
                    y,
                    newPosByDestination.z,
                    playerEntity.getYHeadRot(),
                    playerEntity.getXRot());
        }
    }

    private static ServerLevel getOrCreateSoulDimension(String userUUID, MinecraftServer server)
    {
        //we use the user's UUID as the dimension ID.
        //There can only be one soul dimension per user
        ResourceLocation loc = ResourceLocationHelper.prefix(userUUID);

        //the key used in the map, Map<key,world>
        //if we've already made the dimension, we can grab it straight from server.getLevel
        ResourceKey<Level> worldKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, loc);

        //check to find our special dimension
        ServerLevel soulDimensionForPlayer = server.getLevel(worldKey);

        return soulDimensionForPlayer != null
               ? soulDimensionForPlayer // Found it! return it. Otherwise make a new one
               : DimensionRegistry.createSoulDimension(server, worldKey);
    }


}
