package de.qaware.demo;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/api")
public class EmbeddingResource {

    @Inject
    DocumentIngestor documentIngestor;

    @POST
    @Path("/ingest")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response ingest(String text) {
        return Response.status(Status.CREATED).entity("Text ingested.").build();
    }
}
