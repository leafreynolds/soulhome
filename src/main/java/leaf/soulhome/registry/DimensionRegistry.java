/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome.registry;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Lifecycle;
import leaf.soulhome.SoulHome;
import leaf.soulhome.dimensions.SoulChunkGenerator;
import leaf.soulhome.network.Network;
import leaf.soulhome.network.SyncDimensionListMessage;
import leaf.soulhome.utils.DimensionHelper;
import leaf.soulhome.utils.LogHelper;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.chunk.listener.IChunkStatusListener;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.IServerConfiguration;
import net.minecraft.world.storage.SaveFormat;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;

public class DimensionRegistry
{


    public static RegistryKey<DimensionSettings> DIMENSION_NOISE_SETTINGS;

    public static class DimensionTypes
    {
        public static final RegistryKey<DimensionType> SOUL_DIMENSION_TYPE = RegistryKey.create(Registry.DIMENSION_TYPE_REGISTRY, SoulHome.SOULHOME_LOC);
    }

    public static void registerNoiseSettings()
    {
        DIMENSION_NOISE_SETTINGS = RegistryKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, SoulHome.SOULHOME_LOC);
    }

    public static void registerChunkGenerators()
    {
        Registry.register(Registry.CHUNK_GENERATOR, SoulHome.SOULHOME_LOC, SoulChunkGenerator.providerCodec);
    }

    public static Dimension soulDimensionBuilder(MinecraftServer server, RegistryKey<Dimension> dimensionKey)
    {
        DynamicRegistries registries = server.registryAccess();

        return new Dimension(
                () ->
                {
                    try
                    {
                        return registries.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getOrThrow(DimensionTypes.SOUL_DIMENSION_TYPE);
                    } catch (Exception e)
                    {
                        LogHelper.error(e);
                    }
                    return null;

                },
                new SoulChunkGenerator(server));
    }


    // Once a dimension is created using this method, it will load automatically on server boot
    // Special thanks to the NewTardisMod team. This would have been a nightmare to figure out
    // https://gitlab.com/Spectre0987/TardisMod-1-14/-/tree/1.16
    public static ServerWorld createSoulDimension(MinecraftServer server, RegistryKey<World> worldKey)
    {
        RegistryKey<Dimension> dimensionKey = RegistryKey.create(Registry.LEVEL_STEM_REGISTRY, worldKey.location());

        BiFunction<MinecraftServer, RegistryKey<Dimension>, Dimension> dimensionFactory = DimensionRegistry::soulDimensionBuilder;
        Dimension dimension = dimensionFactory.apply(server, dimensionKey);

        // Refer to META-INF/accesstransformer.cfg here for changing private fields to public
        Executor executor = server.executor;
        SaveFormat.LevelSave levelSave = server.storageSource;
        IChunkStatusListener chunkStatusListener = server.progressListenerFactory.create(11);

        //configs
        IServerConfiguration serverConfiguration = server.getWorldData();
        DimensionGeneratorSettings dimensionGeneratorSettings = serverConfiguration.worldGenSettings();


        // register the dimension
        dimensionGeneratorSettings.dimensions().register(
                dimensionKey,
                dimension,
                Lifecycle.stable() //experimental has issues apparently? Lets avoid that.
        );

        //base the world info on overworld? Not actually sure if that's what I want for soul dimensions
        //todo revisit this later. Don't just forget about it.
        DerivedWorldInfo derivedWorldInfo = new DerivedWorldInfo(serverConfiguration, serverConfiguration.overworldData());

        ServerWorld newSoulWorld = new ServerWorld(
                server,
                executor,
                levelSave,
                derivedWorldInfo,
                worldKey,
                dimension.type(),
                chunkStatusListener,
                dimension.generator(),
                dimensionGeneratorSettings.isDebug(),
                BiomeManager.obfuscateSeed(dimensionGeneratorSettings.seed()),
                ImmutableList.of(),
                false);

        // pay attention to borders?
        // why do we link the soul world borders to the over world borders?
        // todo ask whether this is actually needed
        server.getLevel(World.OVERWORLD).getWorldBorder().addListener(new IBorderListener.Impl(newSoulWorld.getWorldBorder()));

        // add the new dimension to the map, so that it auto loads on server boot.
        // forgeGetWorldMap is marked as deprecated because you can
        // screw up a lot of things if you mess with this map.
        // So be very very careful when touching it.
        Map<RegistryKey<World>, ServerWorld> map = server.forgeGetWorldMap();
        map.put(worldKey, newSoulWorld);

        // increment forge worldArrayMarker, so that the world will tick()
        server.markWorldsDirty();

        //then post an event for our new world. Welcome :)
        MinecraftForge.EVENT_BUS.post(new WorldEvent.Load(newSoulWorld));
        LogHelper.info("New soul dimension has been created: " + dimensionKey.location().toString());

        //put in the platform
        final int PLATFORM_RADIUS = 16;
        for (int x = -PLATFORM_RADIUS; x < PLATFORM_RADIUS; x++)
        {
            for (int z = -PLATFORM_RADIUS; z < PLATFORM_RADIUS; z++)
            {
                newSoulWorld.setBlockAndUpdate(new BlockPos(x, DimensionHelper.FLOOR_LEVEL, z), Blocks.GRASS_BLOCK.defaultBlockState());
                newSoulWorld.setBlockAndUpdate(new BlockPos(x, DimensionHelper.FLOOR_LEVEL - 1, z), Blocks.DIRT.defaultBlockState());
                newSoulWorld.setBlockAndUpdate(new BlockPos(x, DimensionHelper.FLOOR_LEVEL - 2, z), Blocks.DIRT.defaultBlockState());
                newSoulWorld.setBlockAndUpdate(new BlockPos(x, DimensionHelper.FLOOR_LEVEL - 3, z), Blocks.DIRT.defaultBlockState());
                newSoulWorld.setBlockAndUpdate(new BlockPos(x, DimensionHelper.FLOOR_LEVEL - 4, z), Blocks.STONE.defaultBlockState());
            }
        }

        //send a packet to all players, requesting that they refresh their dimension list.
        Network.sendPacketToAll(new SyncDimensionListMessage(worldKey, true));

        //finally return the new world so the player can finish teleporting there
        return newSoulWorld;
    }
}
