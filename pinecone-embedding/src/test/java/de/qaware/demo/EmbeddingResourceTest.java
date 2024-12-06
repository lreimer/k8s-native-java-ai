package de.qaware.demo;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class EmbeddingResourceTest {
    @Test
    void testIngestEndpoint() {
        given()
          .when().body("Hello World.").post("/api/ingest")
          .then()
             .statusCode(201)
             .body(is("Text ingested."));
    }

}