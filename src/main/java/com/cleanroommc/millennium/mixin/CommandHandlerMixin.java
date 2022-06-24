package com.cleanroommc.millennium.mixin;

import com.cleanroommc.millennium.common.command.BrigadierLegacyCommandBridge;
import com.cleanroommc.millennium.common.command.CommandNewHelp;
import com.cleanroommc.millennium.common.command.IBrigadierCommandHandler;
import com.cleanroommc.millennium.common.command.LegacyCommandException;
import com.cleanroommc.millennium.common.event.RegisterCommandsEvent;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.*;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.*;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.function.Consumer;

@Mixin(CommandHandler.class)
public class CommandHandlerMixin implements IBrigadierCommandHandler {
    private final CommandDispatcher<ICommandSender> commandDispatcher = new CommandDispatcher<>();

    private final ImmutableMap<String, Consumer<CommandDispatcher<ICommandSender>>> blacklistedCommandNames = ImmutableMap.of(
            "net.minecraft.command.CommandHelp", CommandNewHelp::register
    );

    @Inject(method = "<init>", at = @At("TAIL"))
    private void doSendRegisterEvent(CallbackInfo ci) {
        if(((CommandHandler)(Object)this) instanceof ClientCommandHandler) {
            MinecraftForge.EVENT_BUS.post(new RegisterCommandsEvent(commandDispatcher, Side.CLIENT));
        } else {
            MinecraftForge.EVENT_BUS.post(new RegisterCommandsEvent(commandDispatcher, Side.SERVER));
        }
    }

    @Inject(method = "registerCommand", at = @At("HEAD"), cancellable = true)
    private void registerTheCommand(ICommand command, CallbackInfoReturnable<ICommand> cir) {
        Consumer<CommandDispatcher<ICommandSender>> replacement = blacklistedCommandNames.get(command.getClass().getName());
        if(replacement != null) {
            replacement.accept(commandDispatcher);
            cir.setReturnValue(command);
            return;
        }
        BrigadierLegacyCommandBridge bridge = new BrigadierLegacyCommandBridge(command);
        LiteralCommandNode<ICommandSender> node = commandDispatcher.register(
                LiteralArgumentBuilder.<ICommandSender>literal(command.getName())
                        .then(RequiredArgumentBuilder.<ICommandSender, String>argument("args", StringArgumentType.greedyString()).executes(bridge))
                        .executes(bridge)
        );
        for(String alias : command.getAliases()) {
            commandDispatcher.register(LiteralArgumentBuilder.<ICommandSender>literal(alias).redirect(node));
        }
        cir.setReturnValue(command);
    }

    @Inject(method = "executeCommand", at = @At("HEAD"), cancellable = true)
    private void executeViaBrigadier(ICommandSender sender, String rawCommand, CallbackInfoReturnable<Integer> cir) {
        if(rawCommand.startsWith("/"))
            rawCommand = rawCommand.substring(1);
        try {
            cir.setReturnValue(commandDispatcher.execute(rawCommand, sender));
        } catch(LegacyCommandException e) {
            TextComponentTranslation text = new TextComponentTranslation(e.getCause().getMessage(), e.getCause().getErrorObjects());
            text.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(text);
            cir.setReturnValue(0);
        } catch(CommandSyntaxException e) {
            TextComponentString text = new TextComponentString(e.getRawMessage().getString());
            text.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(text);
            if (e.getInput() != null && e.getCursor() >= 0) {
                int i = Math.min(e.getInput().length(), e.getCursor());
                TextComponentString mutableText = new TextComponentString("");
                mutableText.getStyle().setColor(TextFormatting.GRAY);
                mutableText.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, rawCommand));
                if (i > 10) {
                    mutableText.appendText("...");
                }
                mutableText.appendText(e.getInput().substring(Math.max(0, i - 10), i));
                if (i < e.getInput().length()) {
                    TextComponentString errorText = new TextComponentString(e.getInput().substring(i));
                    errorText.getStyle().setColor(TextFormatting.RED);
                    errorText.getStyle().setUnderlined(true);
                    mutableText.appendSibling(errorText);
                }
                mutableText.appendSibling(new TextComponentTranslation("command.context.here").setStyle(new Style().setColor(TextFormatting.RED).setItalic(true)));
                sender.sendMessage(mutableText);
            }
            cir.setReturnValue(0);
        }
    }

    @Override
    public CommandDispatcher<ICommandSender> getCommandDispatcher() {
        return commandDispatcher;
    }
}
