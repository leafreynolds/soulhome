/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome.dimensions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import leaf.soulhome.registry.BiomeRegistry;
import leaf.soulhome.utils.DimensionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.core.registries.Registries;
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

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SoulChunkGenerator extends ChunkGenerator
{
    private final Holder<Biome> biome;

    public static final MapCodec<SoulChunkGenerator> providerCodec =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            RegistryFileCodec.<Biome>create(Registries.BIOME, Biome.DIRECT_CODEC)
                                    .fieldOf("biome").forGetter(SoulChunkGenerator::getBiome)
                    ).apply(instance, SoulChunkGenerator::new));


    public SoulChunkGenerator(Holder<Biome> biome)
    {
        super(new FixedBiomeSource(biome));
        this.biome = biome;
    }

    public Holder<Biome> getBiome()
    {
        return biome;
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec()
    {
        return providerCodec;
    }

    @Override
    public void applyCarvers(WorldGenRegion worldGenRegion, long p_223044_, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunkAccess, GenerationStep.Carving carving)
    {

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
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunkAccess)
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
    }

}
