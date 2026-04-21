/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.soulhome.datagen.language;

import leaf.soulhome.SoulHome;
import leaf.soulhome.constants.Constants;
import leaf.soulhome.items.BoundSoulkey;
import leaf.soulhome.utils.ResourceLocationHelper;
import leaf.soulhome.utils.StringHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class EngLangGen extends LanguageProvider
{
    private final PackOutput packOutput;

    public EngLangGen(PackOutput packOutput)
    {
        super(packOutput, SoulHome.MODID, "en_us");
        this.packOutput = packOutput;
    }

    @Override
    protected void addTranslations()
    {
        //Items and Blocks
        for (Item item : BuiltInRegistries.ITEM)
        {
            final ResourceLocation registryName = ResourceLocationHelper.get(item);
            if (registryName != null && registryName.getNamespace().contentEquals(SoulHome.MODID))
            {
                final String path = registryName.getPath();
                String localisedString = StringHelper.fixCapitalisation(path);
                final String tooltipStringKey = String.format(Constants.StringKeys.SOULHOME_ITEM_TOOLTIP, path);
                String tooltipString = "";

                //string overrides
                switch (localisedString)
                {
                    case "Guide":
                        tooltipString = "If patchouli is installed, this is your guide to the mod";
                        break;
                    case "Soulkey":
                        tooltipString = "The key to accessing your own soul. Use for the full duration and it will take you there. As well as anything else nearby.";
                        break;
                }

                if (item instanceof BoundSoulkey)
                {
                    add(item.getDescriptionId(), "Bound Soulkey");
                }
                else
                {
                    add(item.getDescriptionId(), localisedString);
                }
                add(tooltipStringKey, tooltipString);


            }
        }

        //Entities
        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE)
        {
            final ResourceLocation registryName = ResourceLocationHelper.get(type);
            if (registryName != null && registryName.getNamespace().equals(SoulHome.MODID))
            {
                add(type.getDescriptionId(), StringHelper.fixCapitalisation(registryName.getPath()));
            }
        }

        //ItemGroups/Tabs
        add("tabs." + SoulHome.MODID + ".items", "SoulHome");

        //Tooltips
        add(Constants.StringKeys.SHIFT_ITEM_TOOLTIP, "\u00A77Hold \u00A78[\u00A7eShift\u00A78]");
        add(Constants.StringKeys.SHIFT_CONTROL_ITEM_TOOLTIP, "\u00A77Hold \u00A78[\u00A7eShift\u00A78] \u00A77and \u00A78[\u00A7eControl\u00A78]");
        add(Constants.StringKeys.CONTROL_ITEM_TOOLTIP, "\u00A77Hold \u00A78[\u00A7eControl\u00A78]");

        add(Constants.StringKeys.PATCHOULI_NOT_INSTALLED, "Patchouli is not installed");

        //Guide book
        add("soulhome.landing", "They say the soul is infinite. They didn't say how empty it was. Fortunately, we can fill it.");

        add("category.basics", "Basics");
        add("category.multiblock", "Multiblocks");

        add("entry.welcome", "Welcome");
        add("entry.soul", "Soul");
        add("entry.soul_key", "SoulKey");
        add("entry.guide", "Guide");


        //KeyBindings
        add(Constants.StringKeys.KEYS_CATEGORY, "SoulHome");
        add(Constants.StringKeys.KEY_SOUL_CHARGE, "Charge Key To Transport");


        //Advancements

        add("advancements.soulhome.main.title", "SoulHome");
        add("advancements.soulhome.main.description", "Welcome to SoulHome. The way to your inner soul.");

        add("advancements.soulhome.obtained_soul_key.title", "Obtained SoulKey");
        add("advancements.soulhome.obtained_soul_key.description", "By using this key, you can transport yourself and nearby entities to your soul.");

        add("advancements.soulhome.entered_soul_dimension.title", "Enlightened");
        add("advancements.soulhome.entered_soul_dimension.description", "Hey wait, why is it so empty in here?");

        add("advancements.soulhome.obtained_guide.title", "Well Read");
        add("advancements.soulhome.obtained_guide.description", "");

        add("advancements.soulhome.blank.title", "blank");
        add("advancements.soulhome.blank.description", "blank");

        //misc

        add(SoulHome.SOULHOME_LOC.toString(), "SoulHome");
        add("biome.soulhome.soulhome", "SoulHome");

    }

}
