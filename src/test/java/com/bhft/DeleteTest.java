package com.bhft;

import com.bhft.domain.Todo;
import com.bhft.service.HttpService;
import com.bhft.service.HttpServiceImpl;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.bhft.constants.StatusCodes.SUCCESS_DELETE;
import static com.bhft.constants.StatusCodes.SUCCESS_POST;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Метод DELETE должен: ")
public class DeleteTest extends BaseTest {
    public final static Logger log = LogManager.getLogger(DeleteTest.class);

    @DisplayName("вернуть корректный статус код")
    @Test
    public void shouldHaveCorrectStatusCodeAfterDeleteRequestTest() {
        log.info("Precondition: Вставляем 2 сущности с разным значением параметра complete");
        long idTodoCompletePositive = generateId();
        long idTodoCompleteNegative = generateId();

        Todo todoCompletePositive = new Todo(idTodoCompletePositive,
                "delete_completed_true_test", true);
        Todo todoCompleteNegative = new Todo(idTodoCompleteNegative,
                "delete_completed_false_test", false);

        HttpService httpService = new HttpServiceImpl();
        httpService.sendPost(todoCompletePositive).then().statusCode(SUCCESS_POST);
        httpService.sendPost(todoCompleteNegative).then().statusCode(SUCCESS_POST);

        log.info("Step1: удаляем 2 сущности");
        Response responseTodoCompletePositive = httpService.sendDelete(idTodoCompletePositive);
        Response responseTodoCompleteNegative = httpService.sendDelete(idTodoCompleteNegative);

        log.info("Step2: проверяем корректность статус кода");
        assertAll(
                () -> assertEquals(SUCCESS_DELETE, responseTodoCompletePositive.statusCode()),
                () -> assertEquals(SUCCESS_DELETE, responseTodoCompleteNegative.statusCode())
        );
    }
}