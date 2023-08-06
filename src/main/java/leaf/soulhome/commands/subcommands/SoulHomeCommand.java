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
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;

public class SoulHomeCommand extends ModCommand
{

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
    {
        return SINGLE_SUCCESS;
    }

    private static int testSub(CommandContext<CommandSourceStack> context, ServerPlayer player)
    {
        return testSub(context, player.getLevel());
    }

    private static int testSub(CommandContext<CommandSourceStack> context, ServerLevel world)
    {
        CommandSourceStack source = context.getSource();
        source.sendSuccess(Component.translatable("command.soulhome.test.sub"), true);

        return SINGLE_SUCCESS;
    }

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        return Commands.literal("home")
                .requires(context -> context.hasPermission(2))
                .then(Commands.literal("sub")
                        .executes(context -> testSub(context, context.getSource().getPlayerOrException())))
                .executes(context -> teleportToSoul(context, context.getSource().getPlayerOrException()))
                ; // end add
    }

    private static int teleportToSoul(CommandContext<CommandSourceStack> context, ServerPlayer player)
    {
        DimensionHelper.FlipDimension(player, context.getSource().getServer(), null, player.getUUID());

        return Command.SINGLE_SUCCESS;
    }

}