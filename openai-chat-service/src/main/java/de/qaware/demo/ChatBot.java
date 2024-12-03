package de.qaware.demo;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Moderate;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@RegisterAiService(
    tools = ChatBotTools.class,
    chatMemoryProviderSupplier = ChatBotMemoryProvider.class
)
@ApplicationScoped
public interface ChatBot {

    @SystemMessage("""
            You are an AI named Bob answering questions about QAware GmbH.
            Your response must be polite, use the same language as the question, and be relevant to the question.

            When you don't know, respond that you don't know the answer.
            """)
    @Moderate
    String ask(@UserMessage String question);

    @SystemMessage("""
            You are an AI named Alice chatting about QAware GmbH.
            Your responses must be polite, use the same language as the question, and be relevant to the questions.

            When you don't know, respond that you don't know the answer.            
            """)
    String chat(@MemoryId int memoryId, @UserMessage String message);
}