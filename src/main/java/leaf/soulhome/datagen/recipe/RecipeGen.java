/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.datagen.recipe;

import leaf.soulhome.SoulHome;
import leaf.soulhome.registry.ItemsRegistry;
import leaf.soulhome.utils.ResourceLocationHelper;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class RecipeGen extends RecipeProvider implements IConditionBuilder
{
    public RecipeGen(DataGenerator generatorIn)
    {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer)
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


    protected static void addBasicArmorRecipes(Consumer<IFinishedRecipe> consumer, ITag<Item> inputMaterial, @Nullable Item head, @Nullable Item chest, @Nullable Item legs, @Nullable Item feet)
    {
        if (head != null)
        {
            ShapedRecipeBuilder.shaped(head).define('X', inputMaterial).pattern("XXX").pattern("X X").group("helmets").unlockedBy("has_material", has(inputMaterial)).save(consumer);
        }
        if (chest != null)
        {
            ShapedRecipeBuilder.shaped(chest).define('X', inputMaterial).pattern("X X").pattern("XXX").pattern("XXX").group("chestplates").unlockedBy("has_material", has(inputMaterial)).save(consumer);
        }
        if (legs != null)
        {
            ShapedRecipeBuilder.shaped(legs).define('X', inputMaterial).pattern("XXX").pattern("X X").pattern("X X").group("leggings").unlockedBy("has_material", has(inputMaterial)).save(consumer);
        }
        if (feet != null)
        {
            ShapedRecipeBuilder.shaped(feet).define('X', inputMaterial).pattern("X X").pattern("X X").group("boots").unlockedBy("has_material", has(inputMaterial)).save(consumer);
        }
    }

    private void decompressRecipe(Consumer<IFinishedRecipe> consumer, IItemProvider output, ITag<Item> input, String name)
    {
        ShapelessRecipeBuilder.shapeless(output, 9)
                .unlockedBy("has_item", has(output))
                .requires(input)
                .save(consumer, ResourceLocationHelper.prefix("conversions/" + name));
    }

    private ShapedRecipeBuilder compressRecipe(IItemProvider output, ITag<Item> input)
    {
        return ShapedRecipeBuilder.shaped(output)
                .define('I', input)
                .pattern("III")
                .pattern("III")
                .pattern("III")
                .unlockedBy("has_item", has(input));
    }

    protected static void addOreSmeltingRecipes(Consumer<IFinishedRecipe> consumer, IItemProvider ore, Item result, float experience, int time)
    {
        String name = result.getRegistryName().getPath();
        String path = ore.asItem().getRegistryName().getPath();
        CookingRecipeBuilder.smelting(Ingredient.of(ore), result, experience, time).unlockedBy("has_ore", has(ore)).save(consumer, ResourceLocationHelper.prefix(name + "_from_smelting_" + path));
        CookingRecipeBuilder.blasting(Ingredient.of(ore), result, experience, time / 2).unlockedBy("has_ore", has(ore)).save(consumer, ResourceLocationHelper.prefix(name + "_from_blasting_" + path));
    }

    protected static void addCookingRecipes(Consumer<IFinishedRecipe> consumer, IItemProvider inputItem, Item result, float experience, int time)
    {
        String name = result.getRegistryName().getPath();
        CookingRecipeBuilder.smelting(Ingredient.of(inputItem), result, experience, time).unlockedBy("has_item", has(inputItem)).save(consumer, ResourceLocationHelper.prefix(name + "_from_smelting"));
        CookingRecipeBuilder.cooking(Ingredient.of(inputItem), result, experience, time / 2, IRecipeSerializer.SMOKING_RECIPE).unlockedBy("has_item", has(inputItem)).save(consumer, ResourceLocationHelper.prefix(name + "_from_smoking"));
        CookingRecipeBuilder.cooking(Ingredient.of(inputItem), result, experience, time, IRecipeSerializer.CAMPFIRE_COOKING_RECIPE).unlockedBy("has_item", has(inputItem)).save(consumer, ResourceLocationHelper.prefix(name + "_from_campfire"));
    }
}
