/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.datagen;

import leaf.soulhome.SoulHome;
import leaf.soulhome.datagen.advancements.AdvancementGen;
import leaf.soulhome.datagen.items.ItemModelsGen;
import leaf.soulhome.datagen.language.EngLangGen;
import leaf.soulhome.datagen.patchouli.PatchouliGen;
import leaf.soulhome.datagen.recipe.RecipeGen;
import leaf.soulhome.datagen.worldgen.WorldgenProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = SoulHome.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGen
{


    @SubscribeEvent
    public static void onDataGen(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        final PackOutput packOutput = generator.getPackOutput();

        generator.addProvider(true, new EngLangGen(packOutput));

        WorldgenProvider worldgenProvider = new WorldgenProvider(packOutput, event.getLookupProvider());
        generator.addProvider(true, worldgenProvider);
        CompletableFuture<HolderLookup.Provider> lookupWithBiomes = worldgenProvider.getRegistryProvider();

        if (!event.includeClient())
        {
            return;
        }

        generator.addProvider(true, new AdvancementGen(packOutput, lookupWithBiomes));
        generator.addProvider(true, new ItemModelsGen(packOutput, existingFileHelper));
        generator.addProvider(true, new RecipeGen(packOutput, event.getLookupProvider()));

        generator.addProvider(true, new PatchouliGen(packOutput));

    }

}
