/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.items;


import leaf.soulhome.properties.PropTypes;
import net.minecraft.item.Item;

public class BaseItem extends Item
{

    public BaseItem()
    {
        super(PropTypes.Items.SIXTY_FOUR.get().tab(SoulHomeItemGroups.ITEMS));
    }

    public BaseItem(Properties prop)
    {
        super(prop);
    }


}
