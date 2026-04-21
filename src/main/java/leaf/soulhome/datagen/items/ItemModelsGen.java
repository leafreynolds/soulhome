/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.soulhome.datagen.items;

import leaf.soulhome.SoulHome;
import leaf.soulhome.registry.ItemsRegistry;
import leaf.soulhome.utils.ResourceLocationHelper;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class ItemModelsGen extends ItemModelProvider
{

    public ItemModelsGen(PackOutput packOutput, ExistingFileHelper existingFileHelper)
    {
        super(packOutput, SoulHome.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels()
    {
        for (DeferredHolder<Item, ? extends Item> itemHolder : ItemsRegistry.ITEMS.getEntries())
        {
            String path = getPath(itemHolder);
            Item item = itemHolder.get();

            //blocks have their own model rules
            if (item instanceof BlockItem)
            {
                continue;
            }

            //else normal item texture rules apply
            simpleItem(path, path);
        }

    }

    public String getPath(Supplier<? extends Item> itemSupplier)
    {
        final ResourceLocation registryName = ResourceLocationHelper.get(itemSupplier.get());
        return registryName.getPath();
    }

    public ItemModelBuilder simpleItem(String path, String texturePath)
    {
        return this.getBuilder(path)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", modLoc("item/" + texturePath));
    }
}
