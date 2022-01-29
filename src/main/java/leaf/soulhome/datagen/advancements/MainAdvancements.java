/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.soulhome.datagen.advancements;

import leaf.soulhome.SoulHome;
import leaf.soulhome.registry.BiomeRegistry;
import leaf.soulhome.registry.ItemsRegistry;
import leaf.soulhome.utils.ResourceLocationHelper;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.criterion.*;
import net.minecraft.command.FunctionObject;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class MainAdvancements implements Consumer<Consumer<Advancement>>
{
    public MainAdvancements()
    {
    }

    public void accept(Consumer<Advancement> advancementConsumer)
    {
        final String tabName = "main";

        final String titleFormat = "advancements.soulhome.%s.title";
        final String descriptionFormat = "advancements.soulhome.%s.description";
        final String achievementPathFormat = "soulhome:%s/%s";

        Advancement root = Advancement.Builder.advancement()
                .display(ItemsRegistry.SOUL_KEY.get(),
                        new TranslationTextComponent(String.format(titleFormat, tabName)),
                        new TranslationTextComponent(String.format(descriptionFormat, tabName)),
                        new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
                        FrameType.TASK,
                        false,//showToast
                        false,//announceChat
                        false)//hidden
                .addCriterion("tick", new TickTrigger.Instance(EntityPredicate.AndPredicate.ANY))
                .save(advancementConsumer, String.format(achievementPathFormat, tabName, "root"));


        final String obtainedSoulKey = "obtained_soul_key";
        Advancement advancement1 = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        ItemsRegistry.GUIDE.get(),
                        new TranslationTextComponent(String.format(titleFormat, obtainedSoulKey)),
                        new TranslationTextComponent(String.format(descriptionFormat, obtainedSoulKey)),
                        (ResourceLocation)null,
                        FrameType.TASK,
                        true, //showToast
                        true, //announce
                        false)//hidden
                .addCriterion(
                        "has_item",
                        InventoryChangeTrigger.Instance.hasItems(ItemsRegistry.SOUL_KEY.get()))
                .rewards(new AdvancementRewards(50, new ResourceLocation[]{new ResourceLocation("soulhome:guide")}, new ResourceLocation[0], FunctionObject.CacheableFunction.NONE))
                .save(advancementConsumer, String.format(achievementPathFormat, tabName, obtainedSoulKey));



        final String obtainedGuide = "obtained_guide";
        Advancement advancement2 = Advancement.Builder.advancement()
                .parent(advancement1)
                .display(
                        ItemsRegistry.GUIDE.get(),
                        new TranslationTextComponent(String.format(titleFormat, obtainedGuide)),
                        new TranslationTextComponent(String.format(descriptionFormat, obtainedGuide)),
                        (ResourceLocation)null,
                        FrameType.TASK,
                        true, //showToast
                        true, //announce
                        false)//hidden
                .addCriterion(
                        "has_item",
                        InventoryChangeTrigger.Instance.hasItems(ItemsRegistry.GUIDE.get()))
                .rewards(new AdvancementRewards(5, new ResourceLocation[0], new ResourceLocation[0], FunctionObject.CacheableFunction.NONE))
                .save(advancementConsumer, String.format(achievementPathFormat, tabName, obtainedGuide));


        final String enteredSoulDimension = "entered_soul_dimension";
        Advancement advancement3 = Advancement.Builder.advancement()
                .parent(advancement1)
                .display(
                        ItemsRegistry.GUIDE.get(),
                        new TranslationTextComponent(String.format(titleFormat, enteredSoulDimension)),
                        new TranslationTextComponent(String.format(descriptionFormat, enteredSoulDimension)),
                        (ResourceLocation)null,
                        FrameType.TASK,
                        true, //showToast
                        true, //announce
                        false)//hidden
                .addCriterion("entered_soul", PositionTrigger.Instance.located(LocationPredicate.inBiome(RegistryKey.create(Registry.BIOME_REGISTRY, SoulHome.SOULHOME_LOC))))
                .rewards(new AdvancementRewards(5, new ResourceLocation[0], new ResourceLocation[0], FunctionObject.CacheableFunction.NONE))
                .save(advancementConsumer, String.format(achievementPathFormat, tabName, enteredSoulDimension));

        final String blank = "blank";
        Advancement advancement4 = Advancement.Builder.advancement()
                .parent(advancement2)
                .display(
                        ItemsRegistry.GUIDE.get(),
                        new TranslationTextComponent(String.format(titleFormat, blank)),
                        new TranslationTextComponent(String.format(descriptionFormat, blank)),
                        (ResourceLocation)null,
                        FrameType.TASK,
                        true, //showToast
                        true, //announce
                        true)//hidden
                .addCriterion("impossible", new ImpossibleTrigger.Instance())
                .rewards(new AdvancementRewards(5, new ResourceLocation[0], new ResourceLocation[0], FunctionObject.CacheableFunction.NONE))
                .save(advancementConsumer, String.format(achievementPathFormat, tabName, blank));


    }
}