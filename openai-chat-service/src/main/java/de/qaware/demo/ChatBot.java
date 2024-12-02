package de.qaware.demo;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(tools = {ChatBotTools.class})
public interface ChatBot {

    @SystemMessage("""
            You are an AI named Bob answering questions about Qaware GmbH.
            Your response must be polite, use the same language as the question, and be relevant to the question.

            When you don't know, respond that you don't know the answer.
            """)
    String chat(@UserMessage String question);
}