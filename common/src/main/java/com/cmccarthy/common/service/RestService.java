package com.cmccarthy.common.service;

import static io.restassured.RestAssured.given;

import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Service;

@Service
public class RestService {

  public RequestSpecification getRequestSpecification() {
    return given().header("Content-Type", "application/json");
  }
}
