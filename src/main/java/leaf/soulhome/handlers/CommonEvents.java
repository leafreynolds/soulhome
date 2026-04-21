/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.handlers;

import leaf.soulhome.SoulHome;
import leaf.soulhome.commands.SoulCommand;
import leaf.soulhome.utils.DimensionHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;


@EventBusSubscriber(modid = SoulHome.MODID, bus = EventBusSubscriber.Bus.GAME)
public class CommonEvents
{
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event)
    {
        SoulCommand.register(event.getDispatcher());
    }


    @SubscribeEvent
    public static void onLivingHurt(LivingIncomingDamageEvent event)
    {
        final LivingEntity entityLiving = event.getEntity();
        final boolean inSoulDimension = DimensionHelper.isInSoulDimension(entityLiving);

        if (!inSoulDimension)
        {
            return;
        }

        //no fall damage in soul homes for any entity
        if (event.getSource() == entityLiving.damageSources().fall())
        {
            entityLiving.fallDistance = 0;
            event.setCanceled(true);
            return;
        }


        if (entityLiving instanceof Player)
        {
            event.setCanceled(true);
            entityLiving.fallDistance = 0;

            if (event.getSource() == entityLiving.damageSources().fellOutOfWorld())
            {
                DimensionHelper.FlipDimension((Player) entityLiving, entityLiving.getServer(), null, entityLiving.getUUID());
            }

        }
    }
}
