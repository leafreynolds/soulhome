/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.compat.curios;

import leaf.soulhome.items.SoulKeyItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.Optional;
import java.util.function.Predicate;

public class SoulHomeCurios
{

    public static ICurio createKeyCurioProvider()
    {
        return new KeyCurio();
    }

    public static Optional<ImmutableTriple<String, Integer, ItemStack>> getCurioSoulKey(LivingEntity livingEntity)
    {
        Predicate<ItemStack> keyCurio = stack -> stack.getItem() instanceof SoulKeyItem;
        return CuriosApi.getCuriosHelper().findEquippedCurio(keyCurio, livingEntity);
    }

    //get the itemstack of the key the player currently has equipped, if any
    public static ItemStack getCurioEquippedKeyStack(PlayerEntity player)
    {
        if (getCurioSoulKey(player).isPresent())
        {
            return getCurioSoulKey(player).get().getRight();
        }
        return ItemStack.EMPTY;
    }
}
