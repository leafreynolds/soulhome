/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.items;

import leaf.soulhome.compat.patchouli.PatchouliCompat;
import leaf.soulhome.constants.Constants;
import leaf.soulhome.properties.PropTypes;
import leaf.soulhome.registry.ItemsRegistry;
import leaf.soulhome.utils.TextHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
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

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        tooltip.add(getEdition().copy().withStyle(ChatFormatting.GRAY));
    }

    public static Component getEdition()
    {
        if (PatchouliCompat.PatchouliIsPresent())
        {
            try
            {
                return PatchouliAPI.get().getSubtitle(ItemsRegistry.GUIDE.getId());
            }
            catch (IllegalArgumentException e)
            {
                return Component.literal("");
            }
        }
        else
        {
            return TextHelper.createTranslatedText(Constants.StringKeys.PATCHOULI_NOT_INSTALLED);
        }
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn)
    {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if (playerIn instanceof ServerPlayer)
        {
            ServerPlayer serverPlayer = (ServerPlayer) playerIn;
            if (PatchouliCompat.PatchouliIsPresent())
            {
                PatchouliAPI.get().openBookGUI(serverPlayer, ItemsRegistry.GUIDE.getId());
            }
            else
            {
                playerIn.sendSystemMessage(TextHelper.createTranslatedText(Constants.StringKeys.PATCHOULI_NOT_INSTALLED));
            }
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

}