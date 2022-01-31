/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome.registry;

import leaf.soulhome.SoulHome;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomeRegistry
{
    public static ResourceKey<Biome> SOUL_BIOME_KEY;
    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, SoulHome.MODID);
    public static final RegistryObject<Biome> SOUL_BIOME = BIOMES.register(SoulHome.MODID, () -> OverworldBiomes.plains(false,false,false));


    public static void registerBiomeKeys()
    {
        SOUL_BIOME_KEY = ResourceKey.create(Registry.BIOME_REGISTRY, SoulHome.SOULHOME_LOC);
    }

}
