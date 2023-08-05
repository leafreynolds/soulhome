/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.handlers;

import leaf.soulhome.SoulHome;
import leaf.soulhome.commands.SoulCommand;
import leaf.soulhome.utils.DimensionHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = SoulHome.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEvents
{
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event)
    {
        SoulCommand.register(event.getDispatcher());
    }


    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event)
    {
        final LivingEntity entityLiving = event.getEntity();
        if (entityLiving instanceof Player
                && DimensionHelper.isInSoulDimension(entityLiving))
        {
            event.setCanceled(true);
            entityLiving.fallDistance = 0;

            if (event.getSource() == DamageSource.OUT_OF_WORLD)
            {
                DimensionHelper.FlipDimension((Player) entityLiving, entityLiving.getServer(), null);
            }

        }
    }
}