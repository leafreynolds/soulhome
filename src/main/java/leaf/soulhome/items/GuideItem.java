/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.items;

import leaf.soulhome.compat.patchouli.PatchouliCompat;
import leaf.soulhome.constants.Constants;
import leaf.soulhome.properties.PropTypes;
import leaf.soulhome.registry.ItemsRegistry;
import leaf.soulhome.utils.TextHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.patchouli.api.PatchouliAPI;

import javax.annotation.Nonnull;
import java.util.List;

public class GuideItem extends BaseItem
{

    public GuideItem()
    {
        super(PropTypes.Items.ONE.get().rarity(Rarity.RARE));
    }

    public static boolean isOpen()
    {
        return ItemsRegistry.GUIDE.getId().equals(PatchouliAPI.get().getOpenBookGui());
    }


    public static ITextComponent getTitle(ItemStack stack)
    {
        //botania uses this when they are rendering their book title in the world.

        ITextComponent title = stack.getDisplayName();

        String akashicTomeNBT = "akashictome:displayName";
        if (stack.hasTag() && stack.getTag().contains(akashicTomeNBT))
        {
            title = ITextComponent.Serializer.fromJson(stack.getTag().getString(akashicTomeNBT));
        }

        return title;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(getEdition().copy().withStyle(TextFormatting.GRAY));
    }

    public static ITextComponent getEdition()
    {
        if (PatchouliCompat.PatchouliIsPresent())
        {
            return PatchouliAPI.get().getSubtitle(ItemsRegistry.GUIDE.getId());
        }
        else
        {
            return TextHelper.createTranslatedText(Constants.StringKeys.PATCHOULI_NOT_INSTALLED);
        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack stack = playerIn.getItemInHand(handIn);

        if (playerIn instanceof ServerPlayerEntity)
        {
            ServerPlayerEntity player = (ServerPlayerEntity) playerIn;
            PatchouliAPI.get().openBookGUI(player, ItemsRegistry.GUIDE.getId());
        }

        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }
}