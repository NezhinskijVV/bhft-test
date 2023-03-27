package com.bhft.service.specs;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static com.bhft.constants.Endpoints.BASE_URL;
import static com.bhft.constants.Headers.BASIC_AUTHORIZATION;

public class RequestSpec {
    public static final RequestSpecification BASIC_AUTHORIZATION_JSON_REQUEST = RestAssured.given()
            .baseUri(BASE_URL)
            .header(BASIC_AUTHORIZATION)
            .contentType(ContentType.JSON);

    public static final RequestSpecification NO_AUTHORIZATION_JSON_REQUEST = RestAssured.given()
            .baseUri(BASE_URL)
            .contentType(ContentType.JSON);
}