package com.cmccarthy.common.service;

import com.cmccarthy.common.config.TestConfiguration;
import com.cmccarthy.common.utils.LogManager;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import static io.restassured.RestAssured.given;

@Service
public class RestService {

    @Autowired(required = false)
    private TestConfiguration testConfiguration;

    @Autowired(required = false)
    private LogManager logManager;

    @PostConstruct
    public void init() {
        if (testConfiguration != null) {
            setupRestAssuredConfig();
        }
    }

    private void setupRestAssuredConfig() {
        TestConfiguration.ApiConfig apiConfig = testConfiguration.getApi();

        RestAssured.config = RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", apiConfig.getConnectionTimeout())
                        .setParam("http.socket.timeout", apiConfig.getSocketTimeout()));

        if (apiConfig.isLogRequestResponse() && logManager != null) {
            RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        }
    }

    public RequestSpecification getRequestSpecification() {
        return given()
                .header("Content-Type", "application/json")
                .accept("application/json");
    }

    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public Response executeWithRetry(RequestSpecification request, String method, String endpoint) {
        if (logManager != null) {
            logManager.info("Executing " + method + " request to: " + endpoint);
        }

        Response response = switch (method.toUpperCase()) {
            case "GET" -> request.get(endpoint);
            case "POST" -> request.post(endpoint);
            case "PUT" -> request.put(endpoint);
            case "DELETE" -> request.delete(endpoint);
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        };

        if (logManager != null) {
            logManager.info("Response status: " + response.getStatusCode());
            logManager.debug("Response body: " + response.getBody().asString());
        }

        return response;
    }

    public Response get(String endpoint) {
        return executeWithRetry(getRequestSpecification(), "GET", endpoint);
    }

    public Response post(String endpoint, Object body) {
        return executeWithRetry(getRequestSpecification().body(body), "POST", endpoint);
    }

    public Response put(String endpoint, Object body) {
        return executeWithRetry(getRequestSpecification().body(body), "PUT", endpoint);
    }

    public Response delete(String endpoint) {
        return executeWithRetry(getRequestSpecification(), "DELETE", endpoint);
    }
}
