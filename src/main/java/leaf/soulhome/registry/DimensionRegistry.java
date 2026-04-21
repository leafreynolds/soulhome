/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome.registry;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import leaf.soulhome.SoulHome;
import leaf.soulhome.dimensions.SoulChunkGenerator;
import leaf.soulhome.network.Network;
import leaf.soulhome.network.SyncDimensionListMessage;
import leaf.soulhome.utils.DimensionHelper;
import leaf.soulhome.utils.LogHelper;
import leaf.soulhome.mixin.DefrostedRegistry;
import leaf.soulhome.utils.ResourceLocationHelper;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;

public class DimensionRegistry
{
	public static final DeferredRegister<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATORS = DeferredRegister.create(Registries.CHUNK_GENERATOR, SoulHome.MODID);
	public static final ResourceKey<Biome> SOULHOME_BIOME = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(SoulHome.MODID, SoulHome.MODID));
	public static final DeferredHolder<MapCodec<? extends ChunkGenerator>, MapCodec<? extends ChunkGenerator>> CHUNK_GENERATOR = CHUNK_GENERATORS.register(SoulHome.MODID, () -> SoulChunkGenerator.providerCodec);


	public static class DimensionTypes
	{
		public static final ResourceKey<DimensionType> SOUL_DIMENSION_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, SoulHome.SOULHOME_LOC);
	}


	public static LevelStem soulDimensionBuilder(MinecraftServer server, ResourceKey<LevelStem> dimensionKey)
	{
		RegistryAccess registries = server.registryAccess(); // get dynamic registries
		return new LevelStem(
				registries.registryOrThrow(Registries.DIMENSION_TYPE).getHolderOrThrow(DimensionTypes.SOUL_DIMENSION_TYPE),
				new SoulChunkGenerator(registries.registryOrThrow(Registries.BIOME).getHolderOrThrow(BiomeRegistry.SOUL_BIOME_KEY)));
	}


	// Once a dimension is created using this method, it will load automatically on server boot
	// Special thanks to the NewTardisMod team. This would have been a nightmare to figure out
	// https://gitlab.com/Spectre0987/TardisMod-1-14/-/tree/1.16
	public static ServerLevel createSoulDimension(MinecraftServer server, ResourceKey<Level> worldKey, String userUUID)
	{
		ResourceKey<LevelStem> dimensionKey = ResourceKey.create(Registries.LEVEL_STEM, worldKey.location());

		BiFunction<MinecraftServer, ResourceKey<LevelStem>, LevelStem> dimensionFactory = DimensionRegistry::soulDimensionBuilder;
		LevelStem dimension = dimensionFactory.apply(server, dimensionKey);

		// Refer to META-INF/accesstransformer.cfg here for changing private fields to public
		Executor executor = server.executor;
		LevelStorageSource.LevelStorageAccess levelSave = server.storageSource;
		ChunkProgressListener chunkProgressListener = server.progressListenerFactory.create(11);

		//configs
		WorldData serverConfiguration = server.getWorldData();

		// register the dimension
		Registry<LevelStem> dimensionRegistry = server.registryAccess().registryOrThrow(Registries.LEVEL_STEM);
		if (dimensionRegistry instanceof WritableRegistry)
		{
			final WritableRegistry<LevelStem> writableRegistry = (WritableRegistry<LevelStem>) dimensionRegistry;
			boolean wasFrozen = ((DefrostedRegistry) writableRegistry).getFrozen();
			((DefrostedRegistry) writableRegistry).setFrozen(false);
			writableRegistry.register(dimensionKey, dimension, RegistrationInfo.BUILT_IN);

			if (wasFrozen)
			{
				((DefrostedRegistry) writableRegistry).setFrozen(true);
			}
		}
		else
		{
			throw new IllegalStateException("Unable to register dimension '" + dimensionKey.location() + "'! Registry not writable!");
		}

		//base the world info on overworld? Not actually sure if that's what I want for soul dimensions
		//todo revisit this later. Don't just forget about it.
		// ^ LOL
		// ^ it's the year 2025 and I still haven't revisited this

		DerivedLevelData derivedWorldInfo = new DerivedLevelData(serverConfiguration, serverConfiguration.overworldData());

		ServerLevel newSoulWorld = new ServerLevel(
				server,
				executor,
				levelSave,
				derivedWorldInfo,
				worldKey,
				dimension,
				chunkProgressListener,
				serverConfiguration.isDebugWorld(),
				BiomeManager.obfuscateSeed(serverConfiguration.worldGenOptions().seed()),
				ImmutableList.of(),
				false,
				(RandomSequences) null);


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

		// increment worldArrayMarker so that the world will tick()
		server.markWorldsDirty();

		//then post an event for our new world. Welcome :)
		NeoForge.EVENT_BUS.post(new LevelEvent.Load(newSoulWorld));
		LogHelper.info("New soul dimension has been created: " + dimensionKey.location());

		StructurePlaceSettings settings = (new StructurePlaceSettings()).setIgnoreEntities(true).setMirror(Mirror.NONE).setRotation(Rotation.NONE);
		StructureTemplateManager manager = newSoulWorld.getStructureManager();

		// Use the UUID of the player to choose an island structure, different players will get different islands representitive of their 'souls'
		UUID soul = UUID.fromString(userUUID);
		Random rand = new Random(soul.getLeastSignificantBits() ^ soul.getMostSignificantBits());
		int islandStyle = rand.nextInt() % 3; // TODO: add more islands, need to change this value as more islands are added
		ResourceLocation soulIslandLocation = ResourceLocationHelper.prefix("soul_island" + islandStyle);

		Optional<StructureTemplate> templateOptional = manager.get(soulIslandLocation);
		if (templateOptional.isPresent())
		{
			StructureTemplate template = templateOptional.get();
			BlockPos pos = new BlockPos(-template.getSize().getX() / 2, DimensionHelper.FLOOR_LEVEL - template.getSize().getY(), -template.getSize().getZ() / 2);
			template.placeInWorld(newSoulWorld, pos, new BlockPos(0, 0, 0), settings, newSoulWorld.random, 0);
		}
		else
		{
			//put in the platform via legacy method if structure fails to load
			LogHelper.warn("Dimension generated via legacy method!!");
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
		}
		//send a packet to all players, requesting that they refresh their dimension list.
		Network.sendPacketToAll(new SyncDimensionListMessage(worldKey, true));
		//finally return the new world so the player can finish teleporting there
		return newSoulWorld;
	}

	public static ServerLevel createSoulDimension(MinecraftServer server, ResourceKey<Level> worldKey)
	{
		return createSoulDimension(server, worldKey, UUID.randomUUID().toString());
	}
}
