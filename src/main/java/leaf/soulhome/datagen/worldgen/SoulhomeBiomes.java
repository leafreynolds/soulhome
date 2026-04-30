package leaf.soulhome.datagen.worldgen;

import leaf.soulhome.registry.BiomeRegistry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class SoulhomeBiomes
{
    public static void bootstrap(BootstrapContext<Biome> context)
    {
        context.register(BiomeRegistry.SOUL_BIOME_KEY, soulhomeBiome());
    }

    private static Biome soulhomeBiome()
    {
        BiomeSpecialEffects effects = new BiomeSpecialEffects.Builder()
                .skyColor(7907327)
                .fogColor(12638463)
                .waterColor(4159204)
                .waterFogColor(329011)
                .foliageColorOverride(4242482)
                .grassColorOverride(7600187)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .build();

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.5f)
                .downfall(0.4f)
                .specialEffects(effects)
                .mobSpawnSettings(MobSpawnSettings.EMPTY)
                .generationSettings(BiomeGenerationSettings.EMPTY)
                .build();
    }
}
