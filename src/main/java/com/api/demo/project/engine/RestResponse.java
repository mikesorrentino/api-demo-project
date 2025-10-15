package com.api.demo.project.engine;

import io.restassured.response.Response;

/**
 * RestResponse<T> - it implements the methods needed to return relevant details of REST responses as needed for testing.
 * @param <T>
 */
public class RestResponse<T> implements InterfaceRestResponse<T> {
    private T data;
    private Response response;
    private Exception e;

    public RestResponse(Class<T> t, Response response) {
        this.response = response;
        try{
            this.data = t.newInstance();
        }catch (Exception e){
            throw new RuntimeException("There should be a default constructor in the Response POJO");
        }
    }

    /**
     * getContent() - it returns the response body content in String format.
     * @return
     */
    public String getContent() {
        return response.getBody().asString();
    }

    public int getStatusCode() {
        return response.getStatusCode();
    }

    /**
     * isSuccessful() - it validates if the sending request was successful.
     *                  It validates the response status code received against the numerous HTTP status codes
     *                  denoting that request was successfully processed
     * @return
     */
    public boolean isSuccessful() {
        int code = response.getStatusCode();
        if( code == 200 || code == 201 || code == 202 || code == 203 || code == 204 || code == 205) return true;
        return false;
    }

    public String getStatusDescription() {
        return response.getStatusLine();
    }

    public Response getResponse() {
        return response;
    }

    public T getBody() {
        try {
            data = (T) response.getBody().as(data.getClass());
        }catch (Exception e) {
            this.e = e;
        }
        return data;
    }

    /**
     * getException() - In case our response body is not deserialized successfully, we will get an exception.
     *                  The variable e will contain this exception.
     * @return
     */
    public Exception getException() {
        return e;
    }
}

