package com.cleanroommc.millennium.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class BrigadierLegacyCommandBridge implements Command<ICommandSender> {
    private final ICommand command;

    public BrigadierLegacyCommandBridge(ICommand command) {
        this.command = command;
    }

    @Override
    public int run(CommandContext<ICommandSender> context) throws CommandSyntaxException {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (command.checkPermission(server, context.getSource()))
        {
            String[] args;
            try {
                args = StringArgumentType.getString(context, "args").split(" ");
            } catch (IllegalArgumentException e) {
                args = new String[0];
            }

            CommandEvent event = new CommandEvent(command, context.getSource(), args);
            if (MinecraftForge.EVENT_BUS.post(event))
            {
                if (event.getException() != null)
                {
                    System.out.println("handle exception");
                }
                return 0;
            }
            try {
                command.execute(server, context.getSource(), args);
            } catch (CommandException e) {
                throw new LegacyCommandException(e);
            }

            return 1;
        }
        return 0;
    }
}
