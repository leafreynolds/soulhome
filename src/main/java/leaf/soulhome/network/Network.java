/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome.network;

import com.mojang.serialization.Codec;
import leaf.soulhome.SoulHome;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

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
        final BiConsumer<PACKET, PacketBuffer> encoder = (packet, buffer) -> codec.encodeStart(NBTDynamicOps.INSTANCE, packet)
                .result()
                .ifPresent(nbt -> buffer.writeNbt((CompoundNBT) nbt));
        final Function<PacketBuffer, PACKET> decoder = buffer -> codec.parse(NBTDynamicOps.INSTANCE, buffer.readNbt())
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
    public static void sendTo(Object msg, ServerPlayerEntity player)
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

    public static SUpdateTileEntityPacket createTEUpdatePacket(TileEntity tile)
    {
        return new SUpdateTileEntityPacket(tile.getBlockPos(), -1, tile.getUpdateTag());
    }

    public static void sendToAllAround(Object mes, RegistryKey<World> dim, BlockPos pos, int radius)
    {
        NETWORK_CHANNEL.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), radius, dim)), mes);
    }

    public static void sendToAllInWorld(Object mes, ServerWorld world)
    {
        NETWORK_CHANNEL.send(PacketDistributor.DIMENSION.with(world::dimension), mes);
    }

    public static void sendToTrackingTE(Object mes, TileEntity te)
    {
        if (te != null && !te.getLevel().isClientSide)
        {
            NETWORK_CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> te.getLevel().getChunkAt(te.getBlockPos())), mes);
        }
    }
}
