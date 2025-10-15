package com.api.demo.project.engine;

import io.restassured.response.Response;

/**
 * InterfaceRestResponse - it contains all the methods we need
 *                         when we operate on a REST Response.
 * @param <T>
 */
public interface InterfaceRestResponse<T> {
    public T getBody();
    public String getContent();
    public int getStatusCode();
    public boolean isSuccessful();
    public String getStatusDescription();
    public Response getResponse();
    public Exception getException();
}

