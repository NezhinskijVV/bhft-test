package com.bhft.service;

import com.bhft.domain.Todo;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static com.bhft.constants.Endpoints.*;
import static com.bhft.constants.StatusCodes.SUCCESS_DELETE;
import static com.bhft.service.specs.RequestSpec.BASIC_AUTHORIZATION_JSON_REQUEST;

public enum TodosIdGenerator {
    INSTANCE;
    private static final List<Long> testIds = new CopyOnWriteArrayList<>();

    public synchronized long generate() {
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .when()
                .get(GET_ENDPOINT)
                .then().extract().response();

        List<Long> todosIds = response.body().jsonPath().getList(".", Todo.class)
                .stream().map(Todo::getId).collect(Collectors.toList());

        long randomValue = (long) (Math.random() * ((100_000_000) + 1));

        if (todosIds.contains(randomValue) || testIds.contains(randomValue)) {
            return generate();
        }
        testIds.add(randomValue);
        return randomValue;
    }

    public synchronized void remove(long id) {
        testIds.remove(id);

        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .when()
                .get(GET_ENDPOINT)
                .then().extract().response();

        if (response.body().jsonPath().getList(".", Todo.class)
                .stream().map(Todo::getId).anyMatch(toDoId -> toDoId == id)) {

            RestAssured.given()
                    .spec(BASIC_AUTHORIZATION_JSON_REQUEST)
                    .when()
                    .delete(DELETE_ENDPOINT, id)
                    .then()
                    .statusCode(SUCCESS_DELETE);
        }
    }
}