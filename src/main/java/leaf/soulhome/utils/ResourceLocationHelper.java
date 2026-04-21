/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.utils;

import leaf.soulhome.SoulHome;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Locale;

public class ResourceLocationHelper
{
	public static ResourceLocation prefix(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(SoulHome.MODID, path.toLowerCase(Locale.ROOT));
	}

	public static ResourceLocation get(Item item)
	{
		return BuiltInRegistries.ITEM.getKey(item);
	}

	public static ResourceLocation get(Entity entity)
	{
		return BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
	}

	public static ResourceLocation get(Block block)
	{
		return BuiltInRegistries.BLOCK.getKey(block);
	}

	public static ResourceLocation get(EntityType<?> type)
	{
		return BuiltInRegistries.ENTITY_TYPE.getKey(type);
	}

}
