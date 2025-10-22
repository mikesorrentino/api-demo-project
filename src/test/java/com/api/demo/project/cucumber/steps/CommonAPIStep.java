package com.api.demo.project.cucumber.steps;

import com.api.demo.project.helpers.PayloadBuilder;
import com.api.demo.project.storage.MainResponseStorage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonAPIStep {

    private static final Logger log = LoggerFactory.getLogger(PostSteps.class);
    @Value("${baseURL}")
    private String baseUrl;

    @Value("${USERS}")
    private String usersEndpoint;

    @Value("${TOKEN}")
    private String authEndpoint;

    @Autowired
    private PayloadBuilder payloadBuilder;

    @Autowired
    private MainResponseStorage mainResponseStorage;

    @Given("I prepare the request payload {string}")
    public void given_IPrepareTheRequestPayload(String fileName) {

        String body = payloadBuilder.prepareRequestPayload("auth.json");

        Response response = RestAssured
                .given()
                .baseUri(baseUrl)
                .header("Content-Type", "application/json")
                .body(body)
                .log().all()
                .when()
                .post(authEndpoint)
                .then()
                .log().all()
                .extract()
                .response();

        String token = response.jsonPath().getString("token"); // adjust based on your API response
        mainResponseStorage.setBearerToken(token);
    }

//    @Then("The response status code should be {int} and response should contain message {string}")
//    public void verifyStatusCodeAndMessage(int expectedStatusCode, String expectedMessage) {
//        Response response = mainResponseStorage.getLastResponse();
//
//        assertEquals(expectedStatusCode, response.getStatusCode(),
//                "Expected status code " + expectedStatusCode);
//
//        assertTrue(
//                response.getBody().asString().toLowerCase().contains(expectedMessage.toLowerCase()),
//                "Response body should contain '" + expectedMessage + "' (case-insensitive)"
//        );
//    }
}
