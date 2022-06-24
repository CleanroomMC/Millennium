package com.cleanroommc.millennium.network;

import com.cleanroommc.millennium.client.command.SuggestionProviderClient;
import com.cleanroommc.millennium.common.command.CommandManager;
import com.google.common.collect.Lists;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;
import java.util.List;

public class RetrievedSuggestionsMessage implements IMessage {
    private Suggestions suggestions;
    private int idx;
    public RetrievedSuggestionsMessage() {

    }

    public RetrievedSuggestionsMessage(int idx, Suggestions suggestions) {
        this.suggestions = suggestions;
        this.idx = idx;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer theBuf = new PacketBuffer(buf);
        int i = ByteBufUtils.readVarInt(theBuf, 5);
        int j = ByteBufUtils.readVarInt(theBuf, 5);
        StringRange stringrange = StringRange.between(i, i + j);
        int k = ByteBufUtils.readVarInt(theBuf, 5);
        List<Suggestion> list = Lists.newArrayListWithCapacity(k);

        for(int l = 0; l < k; ++l) {
            String s = ByteBufUtils.readUTF8String(theBuf);
            ITextComponent itextcomponent;
            try {
                itextcomponent = theBuf.readBoolean() ? theBuf.readTextComponent() : null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            list.add(new Suggestion(stringrange, s, CommandManager.componentMessage(itextcomponent)));
        }
        this.suggestions = new Suggestions(stringrange, list);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer theBuf = new PacketBuffer(buf);
        ByteBufUtils.writeVarInt(theBuf, this.suggestions.getRange().getStart(), 5);
        ByteBufUtils.writeVarInt(theBuf, this.suggestions.getRange().getLength(), 5);
        ByteBufUtils.writeVarInt(theBuf, this.suggestions.getList().size(), 5);

        for(Suggestion suggestion : this.suggestions.getList()) {
            ByteBufUtils.writeUTF8String(theBuf, suggestion.getText());
            theBuf.writeBoolean(suggestion.getTooltip() != null);
            if (suggestion.getTooltip() != null) {
                theBuf.writeTextComponent(CommandManager.messageComponent(suggestion.getTooltip()));
            }
        }
    }

    static class Handler implements IMessageHandler<RetrievedSuggestionsMessage, IMessage> {

        @Override
        public IMessage onMessage(RetrievedSuggestionsMessage message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                SuggestionProviderClient.instance.provideSuggestions(message.idx, message.suggestions);
            });
            return null;
        }
    }
}
