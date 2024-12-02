package de.qaware.demo;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/api/ask")
public class ChatBotResource {

    @Inject
    ChatBot bot;
  
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String ask(@QueryParam("q") String question) {
        return bot.chat(question);
    }
}
