/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome.dimensions;

import com.mojang.serialization.Codec;
import leaf.soulhome.registry.BiomeRegistry;
import leaf.soulhome.utils.DimensionHelper;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.Blockreader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

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
        super(new SingleBiomeProvider(biomes.getOrThrow(BiomeRegistry.SOUL_BIOME_KEY)), new DimensionStructuresSettings(false));
        this.biomeRegistry = biomes;
    }

    @Override
    public int getSpawnHeight()
    {
        return DimensionHelper.FLOOR_LEVEL;
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec()
    {
        return providerCodec;
    }

    @Override
    public IBlockReader getBaseColumn(int x, int z)
    {
        BlockState[] blockstate = new BlockState[0];
        return new Blockreader(blockstate);
    }

    @Override
    public ChunkGenerator withSeed(long seedIn)
    {
        return this;
    }

    @Override
    public void fillFromNoise(IWorld world, StructureManager structures, IChunk chunk) {    }

    @Override
    public void buildSurfaceAndBedrock(WorldGenRegion region, IChunk chunk) {    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Type heightmapType)
    {
        return 0;
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
    public void applyCarvers(long seed, BiomeManager biomes, IChunk chunk, GenerationStage.Carving carvingStage) {    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion p_230354_1_) {    }

    @Nullable
    @Override
    public BlockPos findNearestMapFeature(ServerWorld world, Structure<?> structure, BlockPos start, int radius, boolean skipExistingChunks)
    {
        return null;
    }

    @Override
    public void applyBiomeDecoration(WorldGenRegion world, StructureManager structures) {    }

    @Override
    public boolean hasStronghold(ChunkPos chunkPos)
    {
        return false;
    }

    @Override
    public void createStructures(DynamicRegistries registries, StructureManager structures, IChunk chunk, TemplateManager templates, long seed) {    }

    @Override
    public void createReferences(ISeedReader world, StructureManager structures, IChunk chunk) {    }
}
