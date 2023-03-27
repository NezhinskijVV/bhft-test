package com.bhft;

import com.bhft.domain.Todo;
import com.bhft.service.HttpService;
import com.bhft.service.HttpServiceImpl;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.bhft.constants.StatusCodes.SUCCESS_POST;
import static com.bhft.constants.StatusCodes.SUCCESS_PUT;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Метод PUT должен: ")
public class PutTest extends BaseTest {
    public final static Logger log = LogManager.getLogger(PutTest.class);

    @DisplayName("вернуть корректный статус код")
    @Test
    public void shouldHaveCorrectStatusCodeAfterPutRequestTest() {
        log.info("Precondition: Вставляем 2 сущности");
        long idTodoCompletePositive = generateId();
        long idTodoCompleteNegative = generateId();

        Todo todoCompletePositive = new Todo(idTodoCompletePositive,
                "put_completed_true_test", true);
        Todo todoCompleteWillNegative = new Todo(idTodoCompleteNegative,
                "put_completed_true_test_2", true);

        HttpService httpService = new HttpServiceImpl();
        httpService.sendPost(todoCompletePositive).then().statusCode(SUCCESS_POST);
        httpService.sendPost(todoCompleteWillNegative).then().statusCode(SUCCESS_POST);

        log.info("Step1: У одной сущности меняем поле complete, у другой меняем значение text");
        todoCompletePositive.setText("new_put_completed_true_test");
        todoCompleteWillNegative.setText("new_put_completed_false_test");
        todoCompleteWillNegative.setCompleted(false);

        Response responseTodoCompletePositive = httpService.sendPut(todoCompletePositive);
        Response responseTodoCompleteNegative = httpService.sendPut(todoCompleteWillNegative);

        log.info("Step2: проверяем корректность статус кода");
        assertAll(
                () -> assertEquals(SUCCESS_PUT, responseTodoCompletePositive.statusCode()),
                () -> assertEquals(SUCCESS_PUT, responseTodoCompleteNegative.statusCode())
        );

        log.info("Post-condition: удаляем добавленные сущности");
        removeById(idTodoCompletePositive);
        removeById(idTodoCompleteNegative);
    }
}