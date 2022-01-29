/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 *
 * Special thank you to the New Tardis Mod team.
 * That mod taught me how to correctly add new commands, among other things!
 * https://tardis-mod.com/books/home/page/links#bkmrk-source
 */

package leaf.soulhome.commands;

import com.mojang.brigadier.CommandDispatcher;
import leaf.soulhome.SoulHome;
import leaf.soulhome.commands.subcommands.SoulHomeCommand;
import leaf.soulhome.commands.subcommands.TestCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;


public class SoulCommand
{

    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal(SoulHome.MODID)
                .then(TestCommand.register(dispatcher))
                .then(SoulHomeCommand.register(dispatcher))
        );
    }
}
