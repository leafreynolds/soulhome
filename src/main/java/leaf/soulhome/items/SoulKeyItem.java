/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.items;

import leaf.soulhome.constants.Constants;
import leaf.soulhome.properties.PropTypes;
import leaf.soulhome.utils.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public class SoulKeyItem extends BaseItem
{
    final int USE_TICKS_REQUIRED = 80;

    public SoulKeyItem()
    {
        super(PropTypes.Items.ONE.get().rarity(Rarity.RARE));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        tooltip.add(TextHelper.createTranslatedText(Constants.StringKeys.KEY_SOUL_CHARGE));
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return USE_TICKS_REQUIRED;
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn)
    {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if (playerIn instanceof ServerPlayer)
        {
            ServerPlayer player = (ServerPlayer) playerIn;
            player.startUsingItem(handIn);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
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
                    player.getUUID()
            );
        }

        return stack;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onUseTick(Level world, LivingEntity livingEntity, ItemStack stack, int count)
    {
        if (livingEntity.level.isClientSide)
        {
            float percentage = MathUtils.clamp01((USE_TICKS_REQUIRED - count) / (float) USE_TICKS_REQUIRED);
            int particlesToCreate = Mth.floor((percentage * percentage * percentage) * USE_TICKS_REQUIRED);

            final float maxRadius = 5;
            float bits = 360f / particlesToCreate;
            float radius = percentage * maxRadius;

            for (int i = particlesToCreate; i >= 0; --i)
            {
                float ang = (bits * i);// + (Math.random() * 10);

                livingEntity.level.addParticle(
                        ParticleTypes.POOF,
                        livingEntity.getX() + Mth.sin(Mth.wrapDegrees(ang)) * radius,
                        livingEntity.getY(),
                        livingEntity.getZ() + Mth.cos(Mth.wrapDegrees(ang)) * radius,
                        0.0D,
                        0.0D,
                        0.0D);
            }
        }
    }

//region Remaining item from crafting, using the soul key as an ingredient. We want to keep the key.
    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack)
    {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack)
    {
        return new ItemStack(stack.getItem());
    }

    //endregion

}