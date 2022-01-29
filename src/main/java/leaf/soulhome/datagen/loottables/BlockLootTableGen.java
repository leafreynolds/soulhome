/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.soulhome.datagen.loottables;

import leaf.soulhome.registry.BlocksRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraftforge.fml.RegistryObject;

public class BlockLootTableGen extends BlockLootTables
{
    @Override
    protected void addTables()
    {
        for (RegistryObject<Block> itemRegistryObject : BlocksRegistry.BLOCKS.getEntries())
        {
            this.dropSelf(itemRegistryObject.get());
        }
    }

    @Override
    protected Iterable<Block> getKnownBlocks()
    {
        return BlocksRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
