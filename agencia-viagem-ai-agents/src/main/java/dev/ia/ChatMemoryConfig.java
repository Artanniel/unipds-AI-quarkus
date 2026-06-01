package main.java.dev.ia;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class ChatMemoryConfig {

    // Produz um bean do tipo ChatMemoryStore para ser injetado na aplicação.
    @Produces
    public ChatMemory chatMemoryStore() {
        return MessageWindowChatMemory.builder()
                .maxMessages(20) // Mantém as últimas 20 interações
                .chatMemoryStore(new InMemoryChatMemoryStore())
                .build();
    }
}
