/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import leaf.soulhome.utils.DimensionHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

public class SoulHomeCommand extends ModCommand
{

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException
    {
        return SINGLE_SUCCESS;
    }

    private static int testSub(CommandContext<CommandSource> context, ServerPlayerEntity player)
    {
        return testSub(context, player.getLevel());
    }

    private static int testSub(CommandContext<CommandSource> context, ServerWorld world)
    {
        CommandSource source = context.getSource();
        source.sendSuccess(new TranslationTextComponent("command.soulhome.test.sub"), true);

        return SINGLE_SUCCESS;
    }

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher)
    {
        return Commands.literal("home")
                .requires(context -> context.hasPermission(2))
                .then(Commands.literal("sub")
                        .executes(context -> testSub(context, context.getSource().getPlayerOrException())))
                .executes(context -> teleportToSoul(context, context.getSource().getPlayerOrException()))
                ; // end add
    }

    private static int teleportToSoul(CommandContext<CommandSource> context, ServerPlayerEntity player)
    {
        DimensionHelper.FlipDimension(player, context.getSource().getServer(), null);

        return Command.SINGLE_SUCCESS;
    }

}