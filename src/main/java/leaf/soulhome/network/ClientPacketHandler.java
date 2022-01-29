/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;

import java.util.Set;

public class ClientPacketHandler
{
    public static void syncDimensionList(SyncDimensionListMessage packet)
    {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        RegistryKey<World> key = packet.getId();
        if (player == null || key == null)
        {
            return;
        }
        Set<RegistryKey<World>> worlds = player.connection.levels();
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
