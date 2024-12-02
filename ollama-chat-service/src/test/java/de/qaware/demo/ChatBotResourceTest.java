package de.qaware.demo;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class ChatBotResourceTest {
    @Test
    void testAskEndpoint() {
        given()
          .when().queryParam("q", "What is QAware GmbH?")
          .get("/api/ask")
          .then()
             .statusCode(200)
             .body(is(notNullValue()));
    }
}