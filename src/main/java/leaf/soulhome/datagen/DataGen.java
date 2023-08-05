/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 * Special Thank you to ChampionAsh5357 from the forge project discord!
 * They provided a series of tutorials with examples of how to add new sections of data generation
 * Generating 20+ different metal related blocks, items, curios etc would have been a nightmare without it.
 */

package leaf.soulhome.datagen;

import leaf.soulhome.SoulHome;
import leaf.soulhome.datagen.advancements.AdvancementGen;
import leaf.soulhome.datagen.blocks.BlockModelsGen;
import leaf.soulhome.datagen.items.ItemModelsGen;
import leaf.soulhome.datagen.language.EngLangGen;
import leaf.soulhome.datagen.loottables.LootTableGen;
import leaf.soulhome.datagen.patchouli.PatchouliGen;
import leaf.soulhome.datagen.recipe.RecipeGen;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = SoulHome.MODID, bus = Bus.MOD)
public class DataGen
{


    @SubscribeEvent
    public static void onDataGen(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(true, new EngLangGen(generator));

        if (!event.includeClient())
        {
            return;
        }

        generator.addProvider(true, new AdvancementGen(generator));
        generator.addProvider(true, new ItemModelsGen(generator, existingFileHelper));
        generator.addProvider(true, new BlockModelsGen(generator, existingFileHelper));
        generator.addProvider(true, new LootTableGen(generator));
        generator.addProvider(true, new RecipeGen(generator));

        generator.addProvider(true, new PatchouliGen(generator));

    }

}
