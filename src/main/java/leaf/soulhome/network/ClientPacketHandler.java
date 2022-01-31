/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Set;

public class ClientPacketHandler
{
    public static void syncDimensionList(SyncDimensionListMessage packet)
    {
        LocalPlayer player = Minecraft.getInstance().player;
        ResourceKey<Level> key = packet.getId();
        if (player == null || key == null)
        {
            return;
        }
        Set<ResourceKey<Level>> worlds = player.connection.levels();
        if (packet.getAdd())
        {
            worlds.add(key);
        }
        else
        {
            worlds.remove(key);
        }
    }
}
