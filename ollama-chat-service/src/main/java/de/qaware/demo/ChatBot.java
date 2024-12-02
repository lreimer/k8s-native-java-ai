package de.qaware.demo;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(
    chatMemoryProviderSupplier = ChatBotMemoryProvider.class
)
public interface ChatBot {

    @SystemMessage("""
            You are an AI named Bob.
            Your response must be polite, use the same language as the question, and be relevant to the question.
            """)
    String ask(@UserMessage String question);

    @SystemMessage("""
            You are an AI named Bob.
            Your responses must be polite, use the same language as the question, and be relevant to the questions.
            """)
    String chat(@MemoryId int memoryId, @UserMessage String message);
}