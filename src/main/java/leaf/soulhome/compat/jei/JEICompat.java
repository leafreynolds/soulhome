/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.compat.jei;

import leaf.soulhome.SoulHome;
import leaf.soulhome.constants.Constants;
import leaf.soulhome.registry.ItemsRegistry;
import leaf.soulhome.utils.ResourceLocationHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JEICompat implements IModPlugin
{

    public static final ResourceLocation JEI = ResourceLocationHelper.prefix("jei");

    @Override
    public ResourceLocation getPluginUid()
    {
        return JEI;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
    {
        //the ability to right click on your tile entity to see what other items interact with it.
        //eg furnace burns fuel, so shows fuel, that kinda thing.
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration)
    {

    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration)
    {
        //for tile entities eg furnace furnace
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration)
    {
        //for (RegistryObject<Item> itemRO : ItemsRegistry.ITEMS.get())
        //reg.addRecipes(TestRecipe.getAllRecipes(world), TestRecipeCategory.NAME);

        addItemInfoPage(registration, ItemsRegistry.SOUL_KEY.get());
        addItemInfoPage(registration, ItemsRegistry.GUIDE.get());

    }

    private void addItemInfoPage(IRecipeRegistration reg, Item item)
    {
        reg.addIngredientInfo(
                new ItemStack(item),
                VanillaTypes.ITEM,
                String.format(Constants.StringKeys.SOULHOME_ITEM_TOOLTIP, item.getRegistryName().getPath()));
    }

}
