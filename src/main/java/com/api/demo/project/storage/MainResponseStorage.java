package com.api.demo.project.storage;

import com.api.demo.project.annotation.LazyComponent;
import io.restassured.response.Response;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Singleton storage component used to temporarily hold data
 * The data stored here will be available during the entire run
 **/
@Data
@Component
public class MainResponseStorage {

    // For example, store here the bearer token, it will then be available for all the scenarios
    // You can then use it like "mainResponseStorage.getBearerToken()"
    private String bearerToken;
    private String payload;
    private String userId;
    private Response lastResponse;

    public String getBearerToken(){
        return bearerToken;
    }
    public void setBearerToken(String bearerToken){
        this.bearerToken = bearerToken;
    }

    public String getPayload() {
        return payload;
    }
    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }
    public Response getLastResponse() { return lastResponse; }
    public void setLastResponse(Response lastResponse) { this.lastResponse = lastResponse; }
}