/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.function.Consumer;

public class SyncDimensionListMessage implements Consumer<Context>
{

    public static final SyncDimensionListMessage INVALID = new SyncDimensionListMessage(null, false);

    public static final Codec<SyncDimensionListMessage> CODEC =
            RecordCodecBuilder.create(instance -> instance
                    .group(World.RESOURCE_KEY_CODEC
                                    .optionalFieldOf("id", null)
                                    .forGetter(SyncDimensionListMessage::getId),
                            Codec.BOOL.fieldOf("add")
                                    .forGetter(SyncDimensionListMessage::getAdd))
                    .apply(instance, SyncDimensionListMessage::new));


    private final RegistryKey<World> id;
    private final boolean add;

    public SyncDimensionListMessage(RegistryKey<World> id, boolean add)
    {
        this.id = id;
        this.add = add;
    }

    public RegistryKey<World> getId()
    {
        return this.id;
    }

    public boolean getAdd()
    {
        return this.add;
    }

    @Override
    public void accept(Context context)
    {
        context.enqueueWork(() -> ClientPacketHandler.syncDimensionList(this));
    }
}
