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

        ShapedRecipeBuilder
                .shaped(ItemsRegistry.PERSONAL_SOUL_KEY.get()) //output
                .define('I', Items.IRON_INGOT)
                .define('E', Items.ENDER_EYE)
                .pattern("I  ") //top row
                .pattern("II ") //middle row
                .pattern("  E") //bottom row
                .unlockedBy("has_material", has(Items.ENDER_EYE))
                .save(consumer);
    }
}
