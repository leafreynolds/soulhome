/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 * Special thank you to SizableShrimp from the Forge Project discord!
 */

package leaf.soulhome.registry;


import leaf.soulhome.SoulHome;
import leaf.soulhome.items.GuideItem;
import leaf.soulhome.items.BoundSoulkey;
import leaf.soulhome.items.SoulKeyItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ItemsRegistry
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, SoulHome.MODID);


    public static final DeferredHolder<Item, Item> SOUL_KEY = ITEMS.register("soulkey", () -> createItem(new SoulKeyItem()));
    public static final DeferredHolder<Item, Item> PERSONAL_SOUL_KEY = ITEMS.register("personal_soulkey", () -> createItem(new BoundSoulkey()));
    public static final DeferredHolder<Item, Item> GUIDE = ITEMS.register("guide", () -> createItem(new GuideItem()));


    private static <T extends Item> T createItem(T item)
    {
        return item;
    }

}
