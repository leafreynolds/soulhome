/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 * Special thank you to SizableShrimp from the Forge Project discord!
 * Java isn't my first programming language, so I didn't know you could collect and set up items like this!
 * Makes setting up items for metals a breeze~
 */

package leaf.soulhome.registry;

import leaf.soulhome.SoulHome;
import leaf.soulhome.items.SoulHomeItemGroups;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class BlocksRegistry
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SoulHome.MODID);
    //public static final RegistryObject<Block> TEST_BLOCK = register("test_block", () -> (new BaseBlock(PropTypes.Blocks.EXAMPLE.get(), SoundType.STONE, 1F, 2F)), Rarity.COMMON);


    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> blockSupplier, Rarity itemRarity)
    {
        RegistryObject<T> registryObject = BLOCKS.register(id, blockSupplier);
        ItemsRegistry.ITEMS.register(id, () -> new BlockItem(registryObject.get(), new Item.Properties().tab(SoulHomeItemGroups.ITEMS).rarity(itemRarity)));


        return registryObject;
    }

}
