/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.items;

import leaf.soulhome.compat.curios.CuriosCompat;
import leaf.soulhome.compat.curios.SoulHomeCurios;
import leaf.soulhome.constants.Constants;
import leaf.soulhome.properties.PropTypes;
import leaf.soulhome.utils.DimensionHelper;
import leaf.soulhome.utils.MathUtils;
import leaf.soulhome.utils.TextHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SoulKeyItem extends BaseItem
{
    final int USE_TICKS_REQUIRED = 80;

    public SoulKeyItem()
    {
        super(PropTypes.Items.ONE.get().rarity(Rarity.RARE));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
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
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if (playerIn instanceof ServerPlayerEntity)
        {
            ServerPlayerEntity player = (ServerPlayerEntity) playerIn;
            player.startUsingItem(handIn);
        }
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    @Nonnull
    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity livingEntity)
    {
        if (!livingEntity.level.isClientSide && livingEntity instanceof PlayerEntity)
        {
            //find all creatures in range
            AxisAlignedBB areaOfEffect = new AxisAlignedBB(livingEntity.blockPosition()).inflate(2.5d);
            List<Entity> entitiesInRange = world.getEntitiesOfClass(Entity.class, areaOfEffect);
            DimensionHelper.FlipDimension((PlayerEntity) livingEntity, livingEntity.getServer(), entitiesInRange);
        }

        return stack;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onUseTick(World world, LivingEntity livingEntity, ItemStack stack, int count)
    {
        if (livingEntity.level.isClientSide)
        {
            float percentage = MathUtils.clamp01((USE_TICKS_REQUIRED - count) / (float) USE_TICKS_REQUIRED);
            int particlesToCreate = MathHelper.floor((percentage * percentage * percentage) * USE_TICKS_REQUIRED);

            final float maxRadius = 5;
            float bits = 360f / particlesToCreate;
            float radius = percentage * maxRadius;

            for (int i = particlesToCreate; i >= 0; --i)
            {
                float ang = (bits * i);// + (Math.random() * 10);

                livingEntity.level.addParticle(
                        ParticleTypes.POOF,
                        livingEntity.getX() + MathHelper.sin(MathHelper.wrapDegrees(ang)) * radius,
                        livingEntity.getY(),
                        livingEntity.getZ() + MathHelper.cos(MathHelper.wrapDegrees(ang)) * radius,
                        0.0D,
                        0.0D,
                        0.0D);
            }
        }
    }

//region Remaining item from crafting, using the soul key as an ingredient. We want to keep the key.
    @Override
    public boolean hasContainerItem(ItemStack stack)
    {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack)
    {
        return new ItemStack(stack.getItem());
    }
//endregion

//region Curio capability stuff. Only used if curios is installed.
    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        if (CuriosCompat.CuriosIsPresent())
        {
            return new ICapabilityProvider()
            {
                final LazyOptional<ICurio> curio = LazyOptional.of(SoulHomeCurios::createKeyCurioProvider);

                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side)
                {
                    return CuriosCapability.ITEM.orEmpty(capability, curio);
                }
            };
        }
        return null;
    }
//endregion
}