/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome.dimensions;

import com.mojang.serialization.Codec;
import leaf.soulhome.registry.BiomeRegistry;
import leaf.soulhome.utils.DimensionHelper;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SoulChunkGenerator extends ChunkGenerator
{
    public static final Codec<SoulChunkGenerator> providerCodec =
            RegistryLookupCodec.create(Registry.BIOME_REGISTRY)
                    .xmap(SoulChunkGenerator::new, SoulChunkGenerator::getBiomeRegistry)
                    .codec();

    private final Registry<Biome> biomeRegistry;

    public SoulChunkGenerator(MinecraftServer server)
    {
        this(server.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY));
    }

    public SoulChunkGenerator(Registry<Biome> biomes)
    {
        super(new FixedBiomeSource(biomes.getOrThrow(BiomeRegistry.SOUL_BIOME_KEY)), new StructureSettings(false));
        this.biomeRegistry = biomes;
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec()
    {
        return providerCodec;
    }


    @Override
    public ChunkGenerator withSeed(long seedIn)
    {
        return this;
    }

    @Override
    public Climate.Sampler climateSampler()
    {
        return null;
    }

    @Override
    public void applyCarvers(WorldGenRegion worldGenRegion, long p_187692_, BiomeManager biomeManager, StructureFeatureManager structureFeatureManager, ChunkAccess chunkAccess, GenerationStep.Carving carving)
    {

    }

    public Registry<Biome> getBiomeRegistry()
    {
        return this.biomeRegistry;
    }

    @Override
    public int getGenDepth()
    {
        return 256;
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion worldGenRegion) {    }

    @Nullable
    @Override
    public BlockPos findNearestMapFeature(ServerLevel world, StructureFeature<?> structure, BlockPos start, int radius, boolean skipExistingChunks)
    {
        return null;
    }

    @Override
    public void buildSurface(WorldGenRegion worldGenRegion, StructureFeatureManager structureFeatureManager, ChunkAccess chunkAccess)
    {

    }

    @Override
    public boolean hasStronghold(ChunkPos chunkPos)
    {
        return false;
    }

    @Override
    public void createStructures(RegistryAccess registries, StructureFeatureManager structures, ChunkAccess chunk, StructureManager templates, long seed) {    }

    @Override
    public void createReferences(WorldGenLevel world, StructureFeatureManager structures, ChunkAccess chunk) {    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, StructureFeatureManager structureFeatureManager, ChunkAccess chunkAccess)
    {
        return CompletableFuture.completedFuture(chunkAccess);
    }

    @Override
    public int getSeaLevel()
    {
        return 0;
    }

    @Override
    public int getMinY()
    {
        return 0;
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types heightmapTypes, LevelHeightAccessor levelHeightAccessor)
    {
        return DimensionHelper.FLOOR_LEVEL;
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor levelHeightAccessor)
    {
        BlockState[] blockstate = new BlockState[0];
        return new NoiseColumn(0, blockstate);
    }
}
