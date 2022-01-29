/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.properties;

import leaf.soulhome.items.SoulHomeItemGroups;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.common.ToolType;

import java.util.function.Supplier;

public class PropTypes
{
    public static class Blocks
    {
        public static final Supplier<Block.Properties> EXAMPLE = () -> Block.Properties.of(Material.GLASS).strength(2.0F, 6.0F).harvestLevel(0).harvestTool(ToolType.PICKAXE);
    }

    public static class Items
    {
        public static final Supplier<Item.Properties> ONE = () -> new Item.Properties().tab(SoulHomeItemGroups.ITEMS).stacksTo(1);

        public static final Supplier<Item.Properties> SIXTEEN = () -> new Item.Properties().tab(SoulHomeItemGroups.ITEMS).stacksTo(16);

        public static final Supplier<Item.Properties> SIXTY_FOUR = () -> new Item.Properties().tab(SoulHomeItemGroups.ITEMS).stacksTo(64);
    }
}
