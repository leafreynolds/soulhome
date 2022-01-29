/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.items;

import leaf.soulhome.SoulHome;
import leaf.soulhome.registry.ItemsRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class SoulHomeItemGroups
{
    public static ItemGroup ITEMS = new ItemGroup(SoulHome.MODID + ".items")
    {
        @Override
        public ItemStack makeIcon()
        {
            return new ItemStack(ItemsRegistry.SOUL_KEY.get());
        }
    };
}
