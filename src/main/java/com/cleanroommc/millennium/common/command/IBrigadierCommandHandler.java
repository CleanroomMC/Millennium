package com.cleanroommc.millennium.common.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.ICommandSender;

public interface IBrigadierCommandHandler {
    CommandDispatcher<ICommandSender> getCommandDispatcher();
}
