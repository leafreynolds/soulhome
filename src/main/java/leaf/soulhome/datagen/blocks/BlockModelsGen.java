/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.soulhome.datagen.blocks;

import leaf.soulhome.SoulHome;
import leaf.soulhome.registry.BlocksRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class BlockModelsGen extends BlockStateProvider
{
    public BlockModelsGen(DataGenerator generator, ExistingFileHelper existingFileHelper)
    {
        super(generator, SoulHome.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
        for (RegistryObject<Block> itemRegistryObject : BlocksRegistry.BLOCKS.getEntries())
        {
            simpleBlock(itemRegistryObject);
        }

    }

    public void simpleBlock(Supplier<? extends Block> blockSupplier)
    {
        simpleBlock(blockSupplier.get());
    }

    public String getPath(Supplier<? extends Block> blockSupplier)
    {
        ResourceLocation location = blockSupplier.get().getRegistryName();
        return location.getPath();
    }

    @Override
    public void simpleBlock(Block block, ModelFile model)
    {
        super.simpleBlock(block, model);
        this.simpleBlockItem(block, model);
    }
}