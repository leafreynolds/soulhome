/*
 * File created ~ 6 - 8 - 2023 ~Leaf
 */

package leaf.soulhome.items;

import leaf.soulhome.utils.CompoundNBTHelper;
import leaf.soulhome.utils.DimensionHelper;
import leaf.soulhome.utils.EntityHelper;
import leaf.soulhome.utils.TextHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public class BoundSoulkey extends SoulKeyItem
{

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn)
	{
		super.appendHoverText(stack, context, tooltip, flagIn);

		final CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		final String soulUUID = "soul_uuid";
		if (tag.hasUUID(soulUUID))
		{
			final UUID soul_uuid = tag.getUUID(soulUUID);
			tooltip.add(
					TextHelper.createTextWithTooltip(
									TextHelper.createText(tag.getString("soul_name")),
									TextHelper.createText(soul_uuid))
							.withStyle(ChatFormatting.GRAY)
			);
		}
	}

	@Override
	public void onCraftedBy(ItemStack itemStack, Level level, Player player)
	{
		if (level.isClientSide)
			return;

		bindKeyToDimension(itemStack, player);
	}

	private static void bindKeyToDimension(ItemStack itemStack, Player player)
	{
		CompoundTag tag = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		tag.putUUID("soul_uuid", player.getUUID());
		tag.putString("soul_name", player.getGameProfile().getName());
		itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
	}

	@Nonnull
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity livingEntity)
	{
		if (!livingEntity.level().isClientSide && livingEntity instanceof Player player)
		{
			//fix creative mode keys
			CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
			if (!tag.hasUUID("soul_uuid"))
			{
				bindKeyToDimension(stack, player);
				tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
			}

			//find all creatures in range
			DimensionHelper.FlipDimension(
					player,
					player.getServer(),
					EntityHelper.getEntitiesInRange(livingEntity, 2.5d, true),
					CompoundNBTHelper.getUuid(tag, "soul_uuid", player.getUUID())
			);
		}

		return stack;
	}
}
