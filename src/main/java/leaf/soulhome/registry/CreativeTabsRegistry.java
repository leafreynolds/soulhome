/*
 * File created ~ 10 - 1 - 2025 ~Leaf
 */

package leaf.soulhome.registry;

import leaf.soulhome.SoulHome;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreativeTabsRegistry
{
	public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SoulHome.MODID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_TABS.register("soulhome", () -> {

		CreativeModeTab.Builder builder = CreativeModeTab.builder()
				.title(Component.translatable("tabs." + SoulHome.MODID + ".items"))
				.icon(ItemsRegistry.SOUL_KEY.get()::getDefaultInstance)
				.displayItems((displayParameters, output) -> {
					for (DeferredHolder<Item, ? extends Item> item : ItemsRegistry.ITEMS.getEntries())
					{
						output.accept(item.get());
					}
				})
				.withTabFactory(SoulhomeCreativeTab::new);

		return builder.build();
	});

	public static class SoulhomeCreativeTab extends CreativeModeTab
	{

		protected SoulhomeCreativeTab(Builder builder)
		{
			super(builder);
		}

		@Override
		public int getLabelColor()
		{
			//todo better colours references
			return 0xFF404040;
		}
	}
}
