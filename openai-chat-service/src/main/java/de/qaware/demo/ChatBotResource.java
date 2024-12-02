package de.qaware.demo;

import dev.langchain4j.agent.tool.P;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/api")
public class ChatBotResource {

    @Inject
    ChatBot bot;
  
    @GET
    @Path("/ask")
    @Produces(MediaType.TEXT_PLAIN)
    public String ask(@QueryParam("q") String question) {
        return bot.ask(question);
    }

    @GET
    @Path("/chat")
    @Produces(MediaType.TEXT_PLAIN)
    public String chat(@QueryParam("id") int id, @QueryParam("msg") String message) {
        return bot.chat(id, message);
    }
}
