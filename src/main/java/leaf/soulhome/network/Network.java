/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome.network;

import com.mojang.serialization.Codec;
import leaf.soulhome.SoulHome;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Network
{
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    private static final SimpleChannel NETWORK_CHANNEL = NetworkRegistry.newSimpleChannel(SoulHome.SOULHOME_LOC, () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void init()
    {
        int id = 0;
        registerCodecPacket(id++, NETWORK_CHANNEL, SyncDimensionListMessage.CODEC, SyncDimensionListMessage.INVALID);
    }

    public static <PACKET extends Consumer<NetworkEvent.Context>> void registerCodecPacket(int id, SimpleChannel channel, Codec<PACKET> codec, PACKET defaultPacket)
    {
        final BiConsumer<PACKET, FriendlyByteBuf> encoder = (packet, buffer) -> codec.encodeStart(NbtOps.INSTANCE, packet)
                .result()
                .ifPresent(nbt -> buffer.writeNbt((CompoundTag) nbt));
        final Function<FriendlyByteBuf, PACKET> decoder = buffer -> codec.parse(NbtOps.INSTANCE, buffer.readNbt())
                .result()
                .orElse(defaultPacket);
        final BiConsumer<PACKET, Supplier<NetworkEvent.Context>> handler = (packet, context) ->
        {
            packet.accept(context.get());
            context.get().setPacketHandled(true);
        };

        final Class<PACKET> packetClass = (Class<PACKET>) (defaultPacket.getClass());

        channel.registerMessage(id, packetClass, encoder, decoder, handler);
    }


    //client side to server
    public static void sendToServer(Object msg)
    {
        NETWORK_CHANNEL.sendToServer(msg);
    }

    //server side to client
    public static void sendTo(Object msg, ServerPlayer player)
    {
        if (!(player instanceof FakePlayer))
        {
            NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), msg);
        }
    }

    public static void sendPacketToAll(Object packet)
    {
        NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(), packet);
    }


    public static void sendToAllAround(Object mes, ResourceKey<Level> dim, BlockPos pos, int radius)
    {
        NETWORK_CHANNEL.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), radius, dim)), mes);
    }

    public static void sendToAllInWorld(Object mes, ServerLevel world)
    {
        NETWORK_CHANNEL.send(PacketDistributor.DIMENSION.with(world::dimension), mes);
    }

    public static void sendToTrackingTE(Object mes, BlockEntity te)
    {
        if (te != null && !te.getLevel().isClientSide)
        {
            NETWORK_CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> te.getLevel().getChunkAt(te.getBlockPos())), mes);
        }
    }
}
