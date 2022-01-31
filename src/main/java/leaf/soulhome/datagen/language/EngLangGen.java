/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.soulhome.datagen.language;

import leaf.soulhome.SoulHome;
import leaf.soulhome.constants.Constants;
import leaf.soulhome.items.SoulHomeItemGroups;
import leaf.soulhome.utils.StringHelper;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class EngLangGen extends LanguageProvider
{
    private final DataGenerator generator;

    public EngLangGen(DataGenerator gen)
    {
        super(gen, SoulHome.MODID, "en_us");
        this.generator = gen;
    }

    @Override
    protected void addTranslations()
    {

        //Items and Blocks
        for (Item item : ForgeRegistries.ITEMS.getValues())
        {
            if (item.getRegistryName().getNamespace().contentEquals(SoulHome.MODID))
            {
                final String path = item.getRegistryName().getPath();
                String localisedString = StringHelper.fixCapitalisation(path);
                final String tooltipStringKey = String.format(Constants.StringKeys.SOULHOME_ITEM_TOOLTIP, path);

                String tooltipString = "";


                //string overrides
                switch (localisedString)
                {
                    case "Guide":
                        //localisedString = "exampleOverride";
                        tooltipString = "If patchouli is installed, this is your guide to the mod";
                        break;
                    case "Soulkey":
                        tooltipString = "The key to accessing your own soul. Use for the full duration and it will take you there. As well as anything else nearby.";
                        break;
                }

                add(tooltipStringKey, tooltipString);
                add(item.getDescriptionId(), localisedString);


            }
        }

        //Entities
        for (EntityType<?> type : ForgeRegistries.ENTITIES)
        {
            if (type.getRegistryName().getNamespace().equals(SoulHome.MODID))
            {
                add(type.getDescriptionId(), StringHelper.fixCapitalisation(type.getRegistryName().getPath()));
            }
        }

        //ItemGroups/Tabs
        add("itemGroup." + SoulHomeItemGroups.ITEMS.getRecipeFolderName(), "SoulHome Items");

        //Damage Sources

        //Containers

        //effects

        //Sound Schemes

        //Configs

        //Commands


        //Tooltips
        add(Constants.StringKeys.SHIFT_ITEM_TOOLTIP, "\u00A77Hold \u00A78[\u00A7eShift\u00A78]");
        add(Constants.StringKeys.SHIFT_CONTROL_ITEM_TOOLTIP, "\u00A77Hold \u00A78[\u00A7eShift\u00A78] \u00A77and \u00A78[\u00A7eControl\u00A78]");
        add(Constants.StringKeys.CONTROL_ITEM_TOOLTIP, "\u00A77Hold \u00A78[\u00A7eControl\u00A78]");

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
