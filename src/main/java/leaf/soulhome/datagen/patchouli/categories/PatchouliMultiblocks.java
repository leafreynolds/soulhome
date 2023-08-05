/*
 * File created ~ 14 - 7 - 2021 ~ Leaf
 */

package leaf.soulhome.datagen.patchouli.categories;


import leaf.soulhome.datagen.patchouli.categories.data.BookStuff;

import java.util.List;

public class PatchouliMultiblocks
{
    public static void collect(List<BookStuff.Category> categories, List<BookStuff.Entry> entries)
    {
        BookStuff.Category multiblocks = new BookStuff.Category(
                "multiblocks",
                "Structures you can build within your soul that will grant you power.",
                "soulhome:guide");

        multiblocks.sortnum = 0;

        categories.add(multiblocks);



        BookStuff.Entry blankEntry = new BookStuff.Entry("Blank", multiblocks, multiblocks.icon);
        blankEntry.pages = new BookStuff.Page[]
                {
                        new BookStuff.TextPage(
                                "Blank Blank Blank! ",blankEntry.icon),
                };
        blankEntry.priority = true;
        blankEntry.sortnum = -10;
        blankEntry.advancement = "soulhome:main/blank";
        entries.add(blankEntry);






    }
}
