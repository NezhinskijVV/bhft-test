package com.bhft.service;

import com.bhft.domain.Todo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static com.bhft.constants.Endpoints.*;
import static com.bhft.service.specs.RequestSpec.BASIC_AUTHORIZATION_JSON_REQUEST;
import static com.bhft.service.specs.RequestSpec.NO_AUTHORIZATION_JSON_REQUEST;

public class HttpServiceImpl implements HttpService {
    @Override
    public Response sendGet() {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .when()
                .get(GET_ENDPOINT);
    }
    @Override
    public Response sendGetWithOffset(int offset) {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .when()
                .param("offset", offset)
                .get(GET_ENDPOINT);
    }

    @Override
    public Response sendGetWithLimit(int limit) {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .when()
                .param("limit", limit)
                .get(GET_ENDPOINT);
    }

    @Override
    public Response sendGetWithOffsetAndLimit(int offset, int limit) {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .when()
                .param("offset", offset)
                .param("limit", limit)
                .get(GET_ENDPOINT);
    }

    @Override
    public Response sendPost(Todo todo) {
        return RestAssured.given()
                .spec(NO_AUTHORIZATION_JSON_REQUEST)
                .body(fromTodoToJson(todo))
                .when()
                .post(POST_ENDPOINT);
    }

    @Override
    public Response sendPut(Todo todo) {
        return RestAssured.given()
                .spec(NO_AUTHORIZATION_JSON_REQUEST)
                .body(fromTodoToJson(todo))
                .when()
                .put(PUT_ENDPOINT, todo.getId());
    }

    @Override
    public Response sendDelete(long id) {
        return RestAssured.given()
                .spec(BASIC_AUTHORIZATION_JSON_REQUEST)
                .when()
                .delete(DELETE_ENDPOINT, id);
    }

    private static String fromTodoToJson(Todo todo) {
        String jsonToDo;
        try {
            jsonToDo = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(todo);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot map: " + todo.toString() + " into json");
        }
        return jsonToDo;
    }
}
