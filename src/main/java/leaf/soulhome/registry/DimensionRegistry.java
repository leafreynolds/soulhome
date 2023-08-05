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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;

public class DimensionRegistry
{
    public static class DimensionTypes
    {
        public static final ResourceKey<DimensionType> SOUL_DIMENSION_TYPE = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, SoulHome.SOULHOME_LOC);
    }

    public static void registerChunkGenerators()
    {
        Registry.register(Registry.CHUNK_GENERATOR, SoulHome.SOULHOME_LOC, SoulChunkGenerator.providerCodec);
    }

    public static LevelStem soulDimensionBuilder(MinecraftServer server, ResourceKey<LevelStem> dimensionKey)
    {
        RegistryAccess registries = server.registryAccess(); // get dynamic registries
        return new LevelStem(
                registries.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getHolderOrThrow(DimensionTypes.SOUL_DIMENSION_TYPE),
                new SoulChunkGenerator(server));
    }


    // Once a dimension is created using this method, it will load automatically on server boot
    // Special thanks to the NewTardisMod team. This would have been a nightmare to figure out
    // https://gitlab.com/Spectre0987/TardisMod-1-14/-/tree/1.16
    public static ServerLevel createSoulDimension(MinecraftServer server, ResourceKey<Level> worldKey)
    {
        ResourceKey<LevelStem> dimensionKey = ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, worldKey.location());

        BiFunction<MinecraftServer, ResourceKey<LevelStem>, LevelStem> dimensionFactory = DimensionRegistry::soulDimensionBuilder;
        LevelStem dimension = dimensionFactory.apply(server, dimensionKey);

        // Refer to META-INF/accesstransformer.cfg here for changing private fields to public
        Executor executor = server.executor;
        LevelStorageSource.LevelStorageAccess levelSave = server.storageSource;
        ChunkProgressListener chunkProgressListener = server.progressListenerFactory.create(11);

        //configs
        WorldData serverConfiguration = server.getWorldData();
        WorldGenSettings worldGenSettings = serverConfiguration.worldGenSettings();

        // register the dimension
        Registry<LevelStem> dimensionRegistry = worldGenSettings.dimensions();
        if (dimensionRegistry instanceof WritableRegistry)
        {
            final WritableRegistry<LevelStem> writableRegistry = (WritableRegistry<LevelStem>) dimensionRegistry;
            writableRegistry.register(dimensionKey, dimension, Lifecycle.stable());
        }
        else
        {
            throw new IllegalStateException("Unable to register dimension '" + dimensionKey.location() + "'! Registry not writable!");
        }

        //base the world info on overworld? Not actually sure if that's what I want for soul dimensions
        //todo revisit this later. Don't just forget about it.
        // ^ LOL
        DerivedLevelData derivedWorldInfo = new DerivedLevelData(serverConfiguration, serverConfiguration.overworldData());

        ServerLevel newSoulWorld = new ServerLevel(
                server,
                executor,
                levelSave,
                derivedWorldInfo,
                worldKey,
                dimension,
                chunkProgressListener,
                worldGenSettings.isDebug(),
                BiomeManager.obfuscateSeed(worldGenSettings.seed()),
                ImmutableList.of(),
                false);


        // pay attention to borders?
        // why do we link the soul world borders to the over world borders?
        // todo ask whether this is actually needed
        server.getLevel(Level.OVERWORLD).getWorldBorder().addListener(new BorderChangeListener.DelegateBorderChangeListener(newSoulWorld.getWorldBorder()));

        // add the new dimension to the map, so that it auto loads on server boot.
        // forgeGetWorldMap is marked as deprecated because you can
        // screw up a lot of things if you mess with this map.
        // So be very very careful when touching it.
        Map<ResourceKey<Level>, ServerLevel> map = server.forgeGetWorldMap();
        map.put(worldKey, newSoulWorld);

        // increment forge worldArrayMarker, so that the world will tick()
        server.markWorldsDirty();

        //then post an event for our new world. Welcome :)
        MinecraftForge.EVENT_BUS.post(new LevelEvent.Load(newSoulWorld));
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
