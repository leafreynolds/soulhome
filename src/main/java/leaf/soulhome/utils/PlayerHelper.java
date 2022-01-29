/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;

import java.util.UUID;

import static net.minecraft.entity.player.PlayerEntity.PERSISTED_NBT_TAG;

public class PlayerHelper
{
    public static String getPlayerName(UUID id, MinecraftServer server)
    {
        GameProfile profileByUUID = server.getProfileCache().get(id);
        return profileByUUID != null ? profileByUUID.getName() : "OFFLINE Player";
    }

    public static CompoundNBT getPersistentTag(PlayerEntity playerEntity, String tag)
    {
        CompoundNBT persistentNBT = CompoundNBTHelper.getOrCreateTag(playerEntity.getPersistentData(), PERSISTED_NBT_TAG);
        return CompoundNBTHelper.getOrCreateTag(persistentNBT, tag);
    }
}
