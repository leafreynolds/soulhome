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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SoulHome.MODID)
public class SoulHome
{
    public static final String MODID = "soulhome";
    public static final ResourceLocation SOULHOME_LOC = ResourceLocationHelper.prefix(SoulHome.MODID);

    public SoulHome()
    {
        LogHelper.info("Registering Soulhome related mcgubbins!");
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(this::commonSetup);
        modBus.addListener(this::loadComplete);

        //DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init));
        //DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::registerIconTextures));
        //DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::retrieveRegisteredIconSprites));


        MinecraftForge.EVENT_BUS.register(this);

        //Register our deferred registries
        BlocksRegistry.BLOCKS.register(modBus);
        ItemsRegistry.ITEMS.register(modBus);
        //EffectsRegistry.EFFECTS.register(modBus);
        //LootModifierRegistry.LOOT_MODIFIERS.register(modBus);
        //AttributesRegistry.ATTRIBUTES.register(modBus);
        //EntityRegistry.ENTITIES.register(modBus);

        //FeatureRegistry.FEATURES.register(modBus);
        //RecipeRegistry.SPECIAL_RECIPES.register(modBus);

        //AdvancementTriggerRegistry.init();

        Network.init();

        // init cross mod compatibility stuff, if relevant
        PatchouliCompat.init();
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            //FeatureRegistry.registerConfiguredFeatures();
            //EntityRegistry.PrepareEntityAttributes();

            BiomeRegistry.registerBiomeKeys();
            DimensionRegistry.registerNoiseSettings();
            DimensionRegistry.registerChunkGenerators();
        });

        //Entity Caps

        DataSerializersRegistry.register();

        LogHelper.info("Common setup complete!");
    }

    private void loadComplete(FMLLoadCompleteEvent event)
    {
        event.enqueueWork(() ->
        {
            //ColorHandler.init();
        });
    }

}
