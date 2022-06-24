package com.cleanroommc.millennium.network;

import com.cleanroommc.millennium.Millennium;
import com.cleanroommc.millennium.common.command.IBrigadierCommandHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import io.netty.buffer.ByteBuf;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class GetSuggestionsMessage implements IMessage {
    private String input = null;
    private int idx = 0;
    public GetSuggestionsMessage() {

    }

    public GetSuggestionsMessage(String input, int idx) {
        this.input = input;
        this.idx = 0;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        input = ByteBufUtils.readUTF8String(buf);
        idx = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, input);
        buf.writeInt(idx);
    }

    static class Handler implements IMessageHandler<GetSuggestionsMessage, RetrievedSuggestionsMessage> {

        @Override
        public RetrievedSuggestionsMessage onMessage(GetSuggestionsMessage message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                StringReader stringreader = new StringReader(message.input);
                if (stringreader.canRead() && stringreader.peek() == '/') {
                    stringreader.skip();
                }
                CommandDispatcher<ICommandSender> dispatcher = ((IBrigadierCommandHandler)player.server.getCommandManager()).getCommandDispatcher();
                ParseResults<ICommandSender> parseresults = dispatcher.parse(stringreader, player.getCommandSenderEntity());
                dispatcher.getCompletionSuggestions(parseresults).thenAccept(suggestions -> {
                    Millennium.CHANNEL.sendTo(new RetrievedSuggestionsMessage(message.idx, suggestions), player);
                });
            });
            return null;
        }
    }

}
