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

public class DeleteSteps {
    private static final Logger log = LoggerFactory.getLogger(PostSteps.class);
    @Value("${baseURL}")
    private String baseUrl;

    @Value("${USERS}")
    private String usersEndpoint;

    @Autowired
    private MainResponseStorage mainResponseStorage;


    // Create the methods for GET, POST, PUT and DELETE using rest assured
    @When("I delete a user")
    public void iDeleteAUser() {
        // String body = payloadBuilder.prepareRequestPayload("createUser.json");
        String token = mainResponseStorage.getBearerToken();
        String userId = mainResponseStorage.getUserId();
        String deleteEndpoint = usersEndpoint + "/" + userId;

        Response response = RestAssured
                .given()
                .baseUri(baseUrl) // baseUrl injected or read from properties
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                //.body(body)
                .log().all()
                .when()
                .delete(deleteEndpoint)
                .then()
                .log().all()
                .extract()
                .response();

        // Optionally save data
        mainResponseStorage.setLastResponse(response);

    }
    @Then("The delete response status code should be {int} and response should contain message {string}")
    public void theDeleteResponseStatusCodeShouldBeAndContainMessage(int expectedStatusCode, String expectedMessage) {
        Response response = mainResponseStorage.getLastResponse();

        assertEquals(expectedStatusCode, response.getStatusCode(), "Expected status code " + expectedStatusCode);
        assertTrue(
                response.getBody().asString().toLowerCase().contains(expectedMessage.toLowerCase()),
                "Response body should contain message '" + expectedMessage + "' (case-insensitive)"
        );
    }
}
