/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.soulhome.datagen.advancements;

import leaf.soulhome.SoulHome;
import leaf.soulhome.registry.ItemsRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;
import java.util.function.Consumer;

public class MainAdvancements implements AdvancementSubProvider
{
    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver)
    {
        final String tabName = "main";

        final String titleFormat = "advancements.soulhome.%s.title";
        final String descriptionFormat = "advancements.soulhome.%s.description";

        AdvancementHolder root = Advancement.Builder.advancement()
                .display(new DisplayInfo(
                        new ItemStack(ItemsRegistry.SOUL_KEY.get()),
                        Component.translatable(String.format(titleFormat, tabName)),
                        Component.translatable(String.format(descriptionFormat, tabName)),
                        Optional.of(ResourceLocation.withDefaultNamespace("textures/gui/advancements/backgrounds/stone.png")),
                        AdvancementType.TASK,
                        false,
                        false,
                        false))
                .addCriterion("tick", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().build()))
                .save(saver, SoulHome.MODID + ":" + tabName + "/root");


        final String obtainedSoulKey = "obtained_soul_key";
        AdvancementHolder advancement1 = Advancement.Builder.advancement()
                .parent(root)
                .display(new DisplayInfo(
                        new ItemStack(ItemsRegistry.GUIDE.get()),
                        Component.translatable(String.format(titleFormat, obtainedSoulKey)),
                        Component.translatable(String.format(descriptionFormat, obtainedSoulKey)),
                        Optional.empty(),
                        AdvancementType.TASK,
                        true,
                        true,
                        false))
                .addCriterion(
                        "has_item",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ItemsRegistry.SOUL_KEY.get()))
                .rewards(AdvancementRewards.Builder.experience(50).build())
                .save(saver, SoulHome.MODID + ":" + tabName + "/" + obtainedSoulKey);


        final String obtainedGuide = "obtained_guide";
        AdvancementHolder advancement2 = Advancement.Builder.advancement()
                .parent(advancement1)
                .display(new DisplayInfo(
                        new ItemStack(ItemsRegistry.GUIDE.get()),
                        Component.translatable(String.format(titleFormat, obtainedGuide)),
                        Component.translatable(String.format(descriptionFormat, obtainedGuide)),
                        Optional.empty(),
                        AdvancementType.TASK,
                        true,
                        true,
                        false))
                .addCriterion(
                        "has_item",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ItemsRegistry.GUIDE.get()))
                .rewards(AdvancementRewards.Builder.experience(5).build())
                .save(saver, SoulHome.MODID + ":" + tabName + "/" + obtainedGuide);


        final String enteredSoulDimension = "entered_soul_dimension";
        HolderGetter<Biome> biomeGetter = registries.lookupOrThrow(Registries.BIOME);
        Holder<Biome> soulhomeBiome = biomeGetter.getOrThrow(ResourceKey.create(Registries.BIOME, SoulHome.SOULHOME_LOC));
        Advancement.Builder.advancement()
                .parent(advancement1)
                .display(new DisplayInfo(
                        new ItemStack(ItemsRegistry.GUIDE.get()),
                        Component.translatable(String.format(titleFormat, enteredSoulDimension)),
                        Component.translatable(String.format(descriptionFormat, enteredSoulDimension)),
                        Optional.empty(),
                        AdvancementType.TASK,
                        true,
                        true,
                        false))
                .addCriterion("entered_soul", PlayerTrigger.TriggerInstance.located(
                        LocationPredicate.Builder.inBiome(soulhomeBiome)))
                .rewards(AdvancementRewards.Builder.experience(5).build())
                .save(saver, SoulHome.MODID + ":" + tabName + "/" + enteredSoulDimension);

    }
}
