/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome.dimensions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import leaf.soulhome.registry.BiomeRegistry;
import leaf.soulhome.utils.DimensionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SoulChunkGenerator extends ChunkGenerator
{
    public static final Codec<SoulChunkGenerator> providerCodec = RecordCodecBuilder.create(builder -> builder.group(
            RegistryOps.retrieveRegistry(Registry.STRUCTURE_SET_REGISTRY).forGetter(SoulChunkGenerator::getStructureSetRegistry),
            RegistryOps.retrieveRegistry(Registry.BIOME_REGISTRY).forGetter(SoulChunkGenerator::getBiomeRegistry)
    ).apply(builder, SoulChunkGenerator::new));

    private final Registry<Biome> biomeRegistry;
    private final Registry<StructureSet> structureSets;

    public SoulChunkGenerator(MinecraftServer server)
    {
        this(server.registryAccess().registryOrThrow(Registry.STRUCTURE_SET_REGISTRY),
                server.registryAccess() // get dynamic registry
                        .registryOrThrow(Registry.BIOME_REGISTRY));
    }


    public SoulChunkGenerator(Registry<StructureSet> structureSets, Registry<Biome> biomes){
        super(structureSets, Optional.empty(), new FixedBiomeSource(biomes.getHolderOrThrow(BiomeRegistry.SOUL_BIOME_KEY)));
        this.structureSets = structureSets;
        this.biomeRegistry = biomes;
    }


    @Override
    protected Codec<? extends ChunkGenerator> codec()
    {
        return providerCodec;
    }

    @Override
    public void applyCarvers(WorldGenRegion worldGenRegion, long p_223044_, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunkAccess, GenerationStep.Carving carving)
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


    @Override
    public void buildSurface(WorldGenRegion worldGenRegion, StructureManager structureManager, RandomState randomState, ChunkAccess chunkAccess)
    {

    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunkAccess)
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
    public int getBaseHeight(int x, int z, Heightmap.Types heightMapTypes, LevelHeightAccessor levelHeightAccessor, RandomState randomState)
    {
        return DimensionHelper.FLOOR_LEVEL;
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor levelHeightAccessor, RandomState randomState)
    {
        return new NoiseColumn(0, new BlockState[0]);
    }

    @Override
    public void addDebugScreenInfo(List<String> p_223175_, RandomState randomState, BlockPos blockPos)
    {
        //??
    }

    public Registry<StructureSet> getStructureSetRegistry()
    {
        return this.structureSets;
    }
}
