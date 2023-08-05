/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.datagen.recipe;

import leaf.soulhome.registry.ItemsRegistry;
import leaf.soulhome.utils.ResourceLocationHelper;
import net.minecraft.data.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import javax.annotation.Nullable;
import java.util.function.Consumer;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;

public class RecipeGen extends RecipeProvider implements IConditionBuilder
{
    public RecipeGen(DataGenerator generatorIn)
    {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer)
    {
        //example recipes obtained from following website on 3rd April '21. Thank you ChampionAsh5357!
        //https://github.com/ChampionAsh5357/1.16.x-Minecraft-Tutorial/blob/1.16.1-32.0.61-web/src/main/java/io/github/championash5357/tutorial/data/TutorialRecipeProvider.java

        //addBasicArmorRecipes(consumer, ItemsRegistry.RUBY.get(), ItemsRegistry.RUBY_HELMET.get(), ItemsRegistry.RUBY_CHESTPLATE.get(), ItemsRegistry.RUBY_LEGGINGS.get(), ItemsRegistry.RUBY_BOOTS.get());
        //ShapedRecipeBuilder.shapedRecipe(BlocksRegistry.WASHER.get()).key('I', Items.IRON_INGOT).key('W', Items.WATER_BUCKET).patternLine("III").patternLine("IWI").patternLine("III").addCriterion("in_water", enteredBlock(Blocks.WATER)).build(consumer);


        //addOreSmeltingRecipes(consumer, BlocksRegistry.GEM_BLOCK.get(), ItemsRegistry.GUIDE.get(), 1.0f, 200);

        ShapedRecipeBuilder
                .shaped(ItemsRegistry.SOUL_KEY.get()) //output
                .define('I', Items.IRON_INGOT)
                .define('E', Items.ENDER_PEARL)
                .pattern("I  ") //top row
                .pattern("II ") //middle row
                .pattern("  E") //bottom row
                .unlockedBy("has_material", has(Items.ENDER_PEARL))
                .save(consumer);

        ShapelessRecipeBuilder
                .shapeless(ItemsRegistry.GUIDE.get())
                .requires(Items.BOOK)
                .requires(ItemsRegistry.SOUL_KEY.get())
                .unlockedBy("has_soul_key", has(ItemsRegistry.SOUL_KEY.get()))
                .save(consumer);



    }
}
