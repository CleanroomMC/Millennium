package com.cleanroommc.millennium.common.command;

import net.minecraft.command.CommandException;

public class LegacyCommandException extends RuntimeException {
    public LegacyCommandException(CommandException e) {
        super(e);
    }

    @Override
    public synchronized CommandException getCause() {
        return (CommandException)super.getCause();
    }
}
