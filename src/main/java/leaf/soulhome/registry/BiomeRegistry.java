/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome.registry;

import leaf.soulhome.SoulHome;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BiomeRegistry
{
	public static ResourceKey<Biome> SOUL_BIOME_KEY = ResourceKey.create(Registries.BIOME, SoulHome.SOULHOME_LOC);
	public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(Registries.BIOME, SoulHome.MODID);
}
