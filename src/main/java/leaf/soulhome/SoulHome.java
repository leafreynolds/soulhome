/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome;

import leaf.soulhome.compat.patchouli.PatchouliCompat;
import leaf.soulhome.network.Network;
import leaf.soulhome.registry.*;
import leaf.soulhome.utils.LogHelper;
import leaf.soulhome.utils.ResourceLocationHelper;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod(SoulHome.MODID)
public class SoulHome
{
    public static final String MODID = "soulhome";
    public static final ResourceLocation SOULHOME_LOC = ResourceLocationHelper.prefix(SoulHome.MODID);

    public SoulHome(IEventBus modBus)
    {
        LogHelper.info("Registering Soulhome related mcgubbins!");

        modBus.addListener(this::commonSetup);
        modBus.addListener(this::loadComplete);

        //Register our deferred registries
        ItemsRegistry.ITEMS.register(modBus);
        CreativeTabsRegistry.CREATIVE_TABS.register(modBus);
        BiomeRegistry.BIOMES.register(modBus);
        DimensionRegistry.CHUNK_GENERATORS.register(modBus);
        DataSerializersRegistry.ENTITY_DATA_SERIALIZERS.register(modBus);

        Network.init(modBus);

        // init cross mod compatibility stuff, if relevant
        PatchouliCompat.init();
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
        {
        });

        LogHelper.info("Common setup complete!");
    }

    private void loadComplete(FMLLoadCompleteEvent event)
    {
        event.enqueueWork(() ->
        {
        });
    }

}
