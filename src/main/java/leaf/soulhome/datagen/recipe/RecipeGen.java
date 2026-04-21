/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.datagen.recipe;

import leaf.soulhome.registry.ItemsRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class RecipeGen extends RecipeProvider
{

    public RecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        ShapedRecipeBuilder
                .shaped(RecipeCategory.TRANSPORTATION, ItemsRegistry.SOUL_KEY.get())
                .define('I', Items.IRON_INGOT)
                .define('E', Items.ENDER_PEARL)
                .pattern("I  ")
                .pattern("II ")
                .pattern("  E")
                .unlockedBy("has_material", has(Items.ENDER_PEARL))
                .save(output);

        ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, ItemsRegistry.GUIDE.get())
                .requires(Items.BOOK)
                .requires(ItemsRegistry.SOUL_KEY.get())
                .unlockedBy("has_soul_key", has(ItemsRegistry.SOUL_KEY.get()))
                .save(output);

        ShapedRecipeBuilder
                .shaped(RecipeCategory.TRANSPORTATION, ItemsRegistry.PERSONAL_SOUL_KEY.get())
                .define('I', Items.IRON_INGOT)
                .define('E', Items.ENDER_EYE)
                .pattern("I  ")
                .pattern("II ")
                .pattern("  E")
                .unlockedBy("has_material", has(Items.ENDER_EYE))
                .save(output);
    }
}
