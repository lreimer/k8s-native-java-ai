package de.qaware.demo;

import dev.langchain4j.agent.tool.Tool;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ChatBotTools {

    @Tool("send email to address with subject and message")
    public void sendEmail(String address, String subject, String message) {
        Log.infof("Sending email to %s about %s", address, subject);
    }

    @Tool("add a and b")
    int sum(int a, int b) {
        Log.infof("Adding %s and %s with tool.", a, b);
        return a + b;
    }
}
