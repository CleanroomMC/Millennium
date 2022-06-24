package com.cleanroommc.millennium.common.command;

import com.google.common.collect.Iterables;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;
import java.util.Map;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandNewHelp {
    private static final SimpleCommandExceptionType FAILED_EXCEPTION = new SimpleCommandExceptionType(CommandManager.componentMessage(new TextComponentTranslation("commands.help.failed")));

    public static void register(CommandDispatcher<ICommandSender> dispatcher) {
        dispatcher.register((CommandManager.literal("help").executes(context -> {
            Map<CommandNode<ICommandSender>, String> map = dispatcher.getSmartUsage(dispatcher.getRoot(), context.getSource());
            for (String string : map.values()) {
                context.getSource().sendMessage(new TextComponentString("/" + string));
            }
            return map.size();
        })).then(CommandManager.argument("command", StringArgumentType.greedyString()).executes(context -> {
            ParseResults<ICommandSender> parseResults = dispatcher.parse(StringArgumentType.getString(context, "command"), context.getSource());
            if (parseResults.getContext().getNodes().isEmpty()) {
                throw FAILED_EXCEPTION.create();
            }
            Map<CommandNode<ICommandSender>, String> map = dispatcher.getSmartUsage(Iterables.getLast(parseResults.getContext().getNodes()).getNode(), context.getSource());
            for (String string : map.values()) {
                context.getSource().sendMessage(new TextComponentString("/" + parseResults.getReader().getString() + " " + string));
            }
            return map.size();
        })));
    }
}
