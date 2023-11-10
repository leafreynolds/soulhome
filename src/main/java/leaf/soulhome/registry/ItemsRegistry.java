/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 * Special thank you to SizableShrimp from the Forge Project discord!
 * Java isn't my first programming language, so I didn't know you could collect and set up items like this!
 * Makes setting up items for metals a breeze~
 */

package leaf.soulhome.registry;


import leaf.soulhome.SoulHome;
import leaf.soulhome.items.GuideItem;
import leaf.soulhome.items.BoundSoulkey;
import leaf.soulhome.items.SoulKeyItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ItemsRegistry
{
    public static final DeferredRegister<net.minecraft.world.item.Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SoulHome.MODID);


    public static final RegistryObject<Item> SOUL_KEY = ITEMS.register("soulkey", () -> createItem(new SoulKeyItem()));
    public static final RegistryObject<Item> PERSONAL_SOUL_KEY = ITEMS.register("personal_soulkey", () -> createItem(new BoundSoulkey()));
    public static final RegistryObject<net.minecraft.world.item.Item> GUIDE = ITEMS.register("guide", () -> createItem(new GuideItem()));


    private static <T extends net.minecraft.world.item.Item> T createItem(T item)
    {
        return item;
    }

}
