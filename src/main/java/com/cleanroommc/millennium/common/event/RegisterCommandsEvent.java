package com.cleanroommc.millennium.common.event;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;

public class RegisterCommandsEvent extends Event {
    private final CommandDispatcher<ICommandSender> dispatcher;
    private final Side side;

    public RegisterCommandsEvent(CommandDispatcher<ICommandSender> dispatcher, Side side) {
        this.dispatcher = dispatcher;
        this.side = side;
    }

    public CommandDispatcher<ICommandSender> getCommandDispatcher() {
        return dispatcher;
    }

    public Side getSide() {
        return side;
    }
}
