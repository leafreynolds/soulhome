/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.soulhome.datagen.patchouli;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import leaf.soulhome.datagen.patchouli.categories.PatchouliBasics;
import leaf.soulhome.datagen.patchouli.categories.PatchouliMultiblocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

//
//  In-Game Documentation generator
//
public class PatchouliGen implements IDataProvider
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final String GUIDE_NAME = "guide";
    private final DataGenerator generator;


    private final List<BookStuff.Category> categories = new ArrayList<>();
    private final List<BookStuff.Entry> entries = new ArrayList<>();

    public PatchouliGen(DataGenerator generatorIn)
    {
        this.generator = generatorIn;
    }

    /**
     * Performs this provider's action.
     */
    public void run(DirectoryCache cache) throws IOException
    {
        Path path = this.generator.getOutputFolder();
        Set<String> entryIDs = Sets.newHashSet();

        Consumer<BookStuff.Entry> entryConsumer = getEntryConsumer(cache, path, entryIDs);
        Consumer<BookStuff.Category> categoryConsumer = getCategoryConsumer(cache, path, entryIDs);

        //adds to our categories and entries fields.
        collectInfoForBook();

        for (BookStuff.Category categoryToConsume : this.categories)
        {
            categoryConsumer.accept(categoryToConsume);
        }

        for (BookStuff.Entry entryToConsume : this.entries)
        {
            entryConsumer.accept(entryToConsume);
        }

    }

    private void collectInfoForBook()
    {
        //dynamically figure out all the things we wanna generate categories/entries for?
        //------------------------------------------//
        //              Categories                  //
        //------------------------------------------//
        //  - Basics                                //
        //  - Curios                                //
        //  - Machines ?                            //
        //  - Challenges ?                          //
        //  -                                       //
        //  -                                       //
        //  -                                       //
        //  -                                       //
        //  -                                       //
        //------------------------------------------//

        PatchouliBasics.collect(this.categories, this.entries);
        PatchouliMultiblocks.collect(this.categories, this.entries);


    }

    private Consumer<BookStuff.Category> getCategoryConsumer(DirectoryCache cache, Path path, Set<String> categoryIDs)
    {
        return category ->
        {
            if (!categoryIDs.add(category.name))
            {
                throw new IllegalStateException("Duplicate page " + category.name);
            }
            else
            {
                Path path1 = getCategoryPath(path, category);

                try
                {
                    IDataProvider.save(GSON, cache, category.serialize(), path1);
                } catch (IOException ioexception)
                {
                    LOGGER.error("Couldn't save page {}", path1, ioexception);
                }

            }
        };
    }

    private Consumer<BookStuff.Entry> getEntryConsumer(DirectoryCache cache, Path path, Set<String> entryIDs)
    {
        return entry ->
        {
            if (!entryIDs.add(entry.name))
            {
                throw new IllegalStateException("Duplicate page " + entry.name);
            }
            else
            {
                Path path1 = getEntryPath(path, entry);

                try
                {
                    IDataProvider.save(GSON, cache, entry.serialize(), path1);
                } catch (IOException ioexception)
                {
                    LOGGER.error("Couldn't save page {}", path1, ioexception);
                }

            }
        };
    }

    private static Path getCategoryPath(Path pathIn, BookStuff.Category category)
    {
        return pathIn.resolve(
                String.format(
                        "data/soulhome/patchouli_books/%s/en_us/categories/%s.json",
                        GUIDE_NAME,
                        category.name));
    }

    private static Path getEntryPath(Path pathIn, BookStuff.Entry entry)
    {
        return pathIn.resolve(
                String.format(
                        "data/soulhome/patchouli_books/%s/en_us/entries/%s/%s.json",
                        GUIDE_NAME,
                        entry.category.name,
                        entry.name));
    }

    /**
     * Gets a name for this provider, to use in logging.
     */
    public String getName()
    {
        return "PatchouliGeneration";
    }


}



