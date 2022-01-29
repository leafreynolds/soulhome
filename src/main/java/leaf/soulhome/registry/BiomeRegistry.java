/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome.registry;

import leaf.soulhome.SoulHome;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeMaker;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomeRegistry
{
    public static RegistryKey<Biome> SOUL_BIOME_KEY;
    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, SoulHome.MODID);
    public static final RegistryObject<Biome> SOUL_BIOME = BIOMES.register(SoulHome.MODID, () -> BiomeMaker.plainsBiome(false));


    public static void registerBiomeKeys()
    {
        SOUL_BIOME_KEY = RegistryKey.create(Registry.BIOME_REGISTRY, SoulHome.SOULHOME_LOC);
    }

}
