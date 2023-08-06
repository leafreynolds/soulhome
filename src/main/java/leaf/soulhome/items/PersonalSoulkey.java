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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public class PersonalSoulkey extends SoulKeyItem
{

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
	{
		super.appendHoverText(stack, worldIn, tooltip, flagIn);

		final CompoundTag stackOrCreateTag = stack.getOrCreateTag();
		final String soulUUID = "soul_uuid";
		if (stackOrCreateTag.hasUUID(soulUUID))
		{
			final UUID soul_uuid = stackOrCreateTag.getUUID(soulUUID);
			tooltip.add(
					TextHelper.createTextWithTooltip(
									TextHelper.createText(stackOrCreateTag.getString("soul_name")),
									TextHelper.createText(soul_uuid))
							.withStyle(ChatFormatting.GRAY)
			);
		}
	}

	@Override
	public void onCraftedBy(ItemStack itemStack, Level level, Player player)
	{
		if(level.isClientSide)
			return;

		itemStack.getOrCreateTag().putUUID("soul_uuid", player.getUUID());
		itemStack.getOrCreateTag().putString("soul_name", player.getGameProfile().getName());
	}


	@Nonnull
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity livingEntity)
	{
		if (!livingEntity.level.isClientSide && livingEntity instanceof Player player)
		{
			//find all creatures in range
			DimensionHelper.FlipDimension(
					player,
					player.getServer(),
					EntityHelper.getEntitiesInRange(livingEntity,2.5d, true),
					CompoundNBTHelper.getUuid(stack.getOrCreateTag(),"soul_uuid", player.getUUID())
			);
		}

		return stack;
	}
}
