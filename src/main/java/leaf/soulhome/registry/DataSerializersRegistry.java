/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.registry;

import leaf.soulhome.SoulHome;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class DataSerializersRegistry
{
    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, SoulHome.MODID);

    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<ResourceLocation>> RESOURCE_LOCATION =
            ENTITY_DATA_SERIALIZERS.register("resource_location", () -> new EntityDataSerializer<>()
            {
                @Override
                public StreamCodec<? super RegistryFriendlyByteBuf, ResourceLocation> codec()
                {
                    return ResourceLocation.STREAM_CODEC.cast();
                }

                @Override
                public ResourceLocation copy(ResourceLocation value)
                {
                    return value;
                }
            });
}