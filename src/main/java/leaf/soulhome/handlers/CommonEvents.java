/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.handlers;

import leaf.soulhome.SoulHome;
import leaf.soulhome.commands.SoulCommand;
import leaf.soulhome.commands.permissions.PermissionManager;
import leaf.soulhome.utils.DimensionHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;


@Mod.EventBusSubscriber(modid = SoulHome.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEvents
{
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event)
    {
        SoulCommand.register(event.getDispatcher());
    }


    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event)
    {
        PermissionManager.init();
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event)
    {
        final LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof PlayerEntity
                && DimensionHelper.isInSoulDimension(entityLiving))
        {
            event.setCanceled(true);
            event.getEntityLiving().fallDistance = 0;

            if (event.getSource() == DamageSource.OUT_OF_WORLD)
            {
                DimensionHelper.FlipDimension((PlayerEntity) entityLiving, entityLiving.getServer(), null);
            }

        }
    }
}