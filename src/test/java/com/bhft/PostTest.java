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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Метод POST должен: ")
public class PostTest extends BaseTest {
    public final static Logger log = LogManager.getLogger(PostTest.class);

    @DisplayName("вернуть корректный статус код")
    @Test
    public void shouldHaveCorrectStatusCodeAfterPostRequestTest() {
        log.info("Step1: Вставляем 2 сущности с разным значением параметра complete");
        long idTodoCompletePositive = generateId();
        long idTodoCompleteNegative = generateId();

        Todo todoCompletePositive = new Todo(idTodoCompletePositive, "post_completed_true_test", true);
        Todo todoCompleteNegative = new Todo(idTodoCompleteNegative, "post_completed_false_test", false);

        HttpService httpService = new HttpServiceImpl();
        Response responseTodoCompletePositive = httpService.sendPost(todoCompletePositive);
        Response responseTodoCompleteNegative = httpService.sendPost(todoCompleteNegative);

        log.info("Step2: Проверяем корректность статус кода");
        assertAll(
                () -> assertEquals(SUCCESS_POST, responseTodoCompletePositive.statusCode()),
                () -> assertEquals(SUCCESS_POST, responseTodoCompleteNegative.statusCode())
        );

        log.info("Post-condition: удаляем добавленные сущности");
        removeById(idTodoCompletePositive);
        removeById(idTodoCompleteNegative);
    }
}