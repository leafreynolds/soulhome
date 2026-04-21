/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class Network
{
    public static void init(IEventBus modBus)
    {
        modBus.addListener(Network::registerPayloads);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event)
    {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                SyncDimensionListMessage.TYPE,
                SyncDimensionListMessage.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> ClientPacketHandler.syncDimensionList(payload))
        );
    }

    //server side to client
    public static void sendTo(CustomPacketPayload msg, ServerPlayer player)
    {
        if (!(player instanceof FakePlayer))
        {
            PacketDistributor.sendToPlayer(player, msg);
        }
    }

    public static void sendPacketToAll(CustomPacketPayload packet)
    {
        PacketDistributor.sendToAllPlayers(packet);
    }

    //client side to server
    public static void sendToServer(CustomPacketPayload msg)
    {
        PacketDistributor.sendToServer(msg);
    }
}
