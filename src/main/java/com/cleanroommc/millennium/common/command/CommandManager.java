package com.cleanroommc.millennium.common.command;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class CommandManager {
    public static LiteralArgumentBuilder<ICommandSender> literal(String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    public static <T> RequiredArgumentBuilder<ICommandSender, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    static class TextComponentMessage implements Message {
        final ITextComponent component;
        public TextComponentMessage(ITextComponent component) {
            this.component = component;
        }

        @Override
        public String getString() {
            return component.getFormattedText();
        }
    }

    public static Message componentMessage(ITextComponent component) {
        return new TextComponentMessage(component);
    }

    public static ITextComponent messageComponent(Message message) {
        if(message instanceof TextComponentMessage)
            return ((TextComponentMessage)message).component;
        else
            return new TextComponentString(message.getString());
    }
}
