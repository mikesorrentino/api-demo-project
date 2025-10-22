package com.api.demo.project.cucumber.steps;

import com.api.demo.project.storage.MainResponseStorage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class GetSteps {

    @Value("${baseURL}")
    private String baseUrl;

    @Value("${USERS}")
    private String usersEndpoint;

    @Autowired
    private MainResponseStorage mainResponseStorage;


    // Create the methods for GET, POST, PUT and DELETE using rest assured
    @When("I get all users")
    public void iGetAllUsers() {
        String token = mainResponseStorage.getBearerToken();
        Response response = RestAssured
                .given()
                .baseUri(baseUrl)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .get(usersEndpoint)
                .then()
                .log().all()
                .extract()
                .response();

        // Optionally save data
        mainResponseStorage.setLastResponse(response);

    }
    @Then("The get response status code shall be {int} and response body should contain {string}")
    public void theGetResponseStatusCodeShallBeAndResponseBodyShouldContain(Integer expectedStatusCode, String expectedText) {

        Response response = mainResponseStorage.getLastResponse();

        assertEquals(expectedStatusCode, response.getStatusCode(),
                "Expected status code " + expectedStatusCode);
        assertTrue(
                response.getBody().asString().toLowerCase().contains(expectedText.toLowerCase()),
                "Response body should contain '" + expectedText + "' (case-insensitive)"
        );
    }
}
