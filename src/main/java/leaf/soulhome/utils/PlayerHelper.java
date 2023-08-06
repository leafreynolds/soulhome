/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;
import java.util.UUID;

import static net.minecraft.world.entity.player.Player.PERSISTED_NBT_TAG;

public class PlayerHelper
{
    public static String getPlayerName(UUID id, MinecraftServer server)
    {
        if (server != null)
        {
            final Optional<GameProfile> gameProfile = server.getProfileCache().get(id);
            if (gameProfile.isPresent())
            {
                return gameProfile.get().getName();
            }
        }
        return "OFFLINE Player";
    }

	public static CompoundTag getPersistentTag(Player playerEntity, String tag)
	{
		CompoundTag persistentNBT = CompoundNBTHelper.getOrCreateTag(playerEntity.getPersistentData(), PERSISTED_NBT_TAG);
		return CompoundNBTHelper.getOrCreateTag(persistentNBT, tag);
	}
}
