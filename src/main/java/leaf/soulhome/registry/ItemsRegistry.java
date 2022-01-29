/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 * Special thank you to SizableShrimp from the Forge Project discord!
 * Java isn't my first programming language, so I didn't know you could collect and set up items like this!
 * Makes setting up items for metals a breeze~
 */

package leaf.soulhome.registry;


import leaf.soulhome.SoulHome;
import leaf.soulhome.items.GuideItem;
import leaf.soulhome.items.SoulKeyItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ItemsRegistry
{
    public static final DeferredRegister<net.minecraft.item.Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SoulHome.MODID);

    //other items

    public static final RegistryObject<net.minecraft.item.Item> SOUL_KEY = ITEMS.register("soulkey", () -> createItem(new SoulKeyItem()));
    public static final RegistryObject<net.minecraft.item.Item> GUIDE = ITEMS.register("guide", () -> createItem(new GuideItem()));


    private static <T extends net.minecraft.item.Item> T createItem(T item)
    {
        return item;
    }

}
