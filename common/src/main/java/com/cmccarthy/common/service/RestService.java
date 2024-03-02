package com.cmccarthy.common.service;

import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Service;

import static io.restassured.RestAssured.given;

@Service
public class RestService {

    public RequestSpecification getRequestSpecification() {
        return given().header("Content-Type", "application/json");
    }
}
