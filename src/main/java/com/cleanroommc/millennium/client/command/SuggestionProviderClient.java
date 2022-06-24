package com.cleanroommc.millennium.client.command;

import com.cleanroommc.millennium.Millennium;
import com.cleanroommc.millennium.network.GetSuggestionsMessage;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.network.NetHandlerPlayClient;

import java.util.concurrent.CompletableFuture;

public class SuggestionProviderClient {
    public static SuggestionProviderClient instance = null;
    private final NetHandlerPlayClient connection;
    private CompletableFuture<Suggestions> future = null;
    private int currentIdx = 0;
    public SuggestionProviderClient(NetHandlerPlayClient connection) {
        this.connection = connection;
    }
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<SuggestionProviderClient> context, SuggestionsBuilder suggestionsBuilder) {
        return getSuggestionsFromServer(context, suggestionsBuilder);
    }
    public CompletableFuture<Suggestions> getSuggestionsFromServer(CommandContext<SuggestionProviderClient> context, SuggestionsBuilder suggestionsBuilder) {
        if (this.future != null) {
            this.future.cancel(false);
        }
        this.future = new CompletableFuture<>();
        int idx = ++this.currentIdx;
        Millennium.CHANNEL.sendToServer(new GetSuggestionsMessage(context.getInput(), idx));
        return this.future;
    }

    public void provideSuggestions(int idx, Suggestions suggestions) {
        if(idx == currentIdx) {
            this.future.complete(suggestions);
            this.future = null;
            this.currentIdx = -1;
        }
    }

}
