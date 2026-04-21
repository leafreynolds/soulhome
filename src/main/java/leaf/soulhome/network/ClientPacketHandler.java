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
        if (player == null || packet.id() == null)
        {
            return;
        }
        ResourceKey<Level> key = packet.levelKey();
        Set<ResourceKey<Level>> worlds = player.connection.levels();
        if (packet.add())
        {
            worlds.add(key);
        }
        else
        {
            worlds.remove(key);
        }
    }
}
