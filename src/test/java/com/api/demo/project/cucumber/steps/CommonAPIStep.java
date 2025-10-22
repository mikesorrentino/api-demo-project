package com.api.demo.project.cucumber.steps;

import com.api.demo.project.helpers.PayloadBuilder;
import com.api.demo.project.storage.MainResponseStorage;
import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class CommonAPIStep {

    private static final Logger log = LoggerFactory.getLogger(PostSteps.class);
    @Value("${baseURL}")
    private String baseUrl;

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

        String token = response.jsonPath().getString("token");
        mainResponseStorage.setBearerToken(token);
    }

}
