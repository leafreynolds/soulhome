/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome.registry;

import leaf.soulhome.SoulHome;
import leaf.soulhome.dimensions.SoulDimensionRenderInfo;
import leaf.soulhome.utils.ResourceLocationHelper;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = SoulHome.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ClientRegistry
{
    public static final ResourceLocation SOUL_SKY_PROPERTY_LOC = ResourceLocationHelper.prefix("soul_sky_property");

    @SubscribeEvent
    public static void register(FMLClientSetupEvent event)
    {
        DimensionSpecialEffects.EFFECTS.put(SOUL_SKY_PROPERTY_LOC, new SoulDimensionRenderInfo());
    }
}
