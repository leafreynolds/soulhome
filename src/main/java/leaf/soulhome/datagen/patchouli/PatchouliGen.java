/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.soulhome.datagen.patchouli;

import leaf.soulhome.SoulHome;
import leaf.soulhome.datagen.patchouli.categories.PatchouliBasics;
import leaf.soulhome.datagen.patchouli.categories.PatchouliMultiblocks;
import leaf.soulhome.datagen.patchouli.categories.data.PatchouliProvider;
import net.minecraft.data.DataGenerator;

//
//  In-Game Documentation generator
//
public class PatchouliGen extends PatchouliProvider
{
    public PatchouliGen(DataGenerator generatorIn)
    {
        super(generatorIn, SoulHome.MODID);
    }


    @Override
    protected void collectInfoForBook()
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

    /**
     * Gets a name for this provider, to use in logging.
     */
    public String getName()
    {
        return "PatchouliGeneration";
    }


}



