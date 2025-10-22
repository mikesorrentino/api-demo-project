package com.api.demo.project.cucumber.steps;

import com.api.demo.project.helpers.PayloadBuilder;
import com.api.demo.project.storage.MainResponseStorage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PUTSteps {
    private static final Logger log = LoggerFactory.getLogger(PostSteps.class);
    @Value("${baseURL}")
    private String baseUrl;

    @Value("${USERS}")
    private String usersEndpoint;

    @Autowired
    private PayloadBuilder payloadBuilder;

    @Autowired
    private MainResponseStorage mainResponseStorage;





    // Create the methods for GET, POST, PUT and DELETE using rest assured
    @When("I change user name")
    public void iChangeUserName() {
         String body = payloadBuilder.prepareRequestPayload("putUser.json");
        String token = mainResponseStorage.getBearerToken();
        String userId = mainResponseStorage.getUserId();
        String putEndpoint = usersEndpoint + "/" + userId;

        Response response = RestAssured
                .given()
                .baseUri(baseUrl) // baseUrl injected or read from properties
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(body)
                .log().all()
                .when()
                .put(putEndpoint)
                .then()
                .log().all()
                .extract()
                .response();

        // Optionally save data
        mainResponseStorage.setLastResponse(response);

    }
    @Then("The response status code should be {int} and response should contain message {string}")
    public void theResponseStatusCodeShallBeAndContainMessage(int expectedStatusCode, String expectedMessage) {
        Response response = mainResponseStorage.getLastResponse();

        assertEquals(expectedStatusCode, response.getStatusCode(), "Expected status code " + expectedStatusCode);
        assertTrue(
                response.getBody().asString().toLowerCase().contains(expectedMessage.toLowerCase()),
                "Response body should contain message '" + expectedMessage + "' (case-insensitive)"
        );
    }
}
