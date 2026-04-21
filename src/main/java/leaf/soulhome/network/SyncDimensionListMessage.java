/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome.network;

import io.netty.buffer.ByteBuf;
import leaf.soulhome.SoulHome;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public record SyncDimensionListMessage(ResourceLocation id, boolean add) implements CustomPacketPayload
{
    public static final Type<SyncDimensionListMessage> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(SoulHome.MODID, "sync_dimension_list"));

    public static final StreamCodec<ByteBuf, SyncDimensionListMessage> STREAM_CODEC =
            StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC, SyncDimensionListMessage::id,
                    ByteBufCodecs.BOOL, SyncDimensionListMessage::add,
                    SyncDimensionListMessage::new
            );

    public SyncDimensionListMessage(ResourceKey<Level> key, boolean add)
    {
        this(key.location(), add);
    }

    public ResourceKey<Level> levelKey()
    {
        return ResourceKey.create(Registries.DIMENSION, id);
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
