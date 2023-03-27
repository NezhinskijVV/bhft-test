package com.bhft;

import com.bhft.domain.Todo;
import com.bhft.service.HttpServiceImpl;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static com.bhft.constants.StatusCodes.SUCCESS_GET;
import static com.bhft.constants.StatusCodes.SUCCESS_POST;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Метод GET должен: ")
public class GetTest extends BaseTest {
    public final static Logger log = LogManager.getLogger(GetTest.class);

    @DisplayName("вернуть корректный статус код")
    @Test
    public void shouldHaveCorrectStatusCodeAfterGetRequestTest() {
        log.info("Выполняем метод GET и проверяем статус код");
        new HttpServiceImpl().sendGet().then().statusCode(SUCCESS_GET);

    }

    @DisplayName("c параметром offset пропустить корректное количество todo")
    @Test
    public void shouldHaveCorrectMissingWithOffsetAfterGetRequestTest() {
        log.info("Precondition: Вставляем 2 сущности");
        long id1 = generateId();
        long id2 = generateId();

        HttpServiceImpl httpService = new HttpServiceImpl();
        httpService.sendPost(new Todo(id1, "get_offset_1", true))
                .then().statusCode(SUCCESS_POST);
        httpService.sendPost(new Todo(id2, "get_offset_2", false))
                .then().statusCode(SUCCESS_POST);

        log.info("Step1: получаем список без параметра offset");
        List<Todo> listGet = toList(httpService.sendGet());

        log.info("Step2: получаем разность спиков исходного и с пропуском одного/всех/всех+1 элементов");
        List<Todo> listGetMinusListGetWithOffset1 = firstListMinusSecond(listGet,
                toList(httpService.sendGetWithOffset(1)));
        List<Todo> listGetMinusListGetWithOffsetEqualsElemsCnt = firstListMinusSecond(listGet,
                toList(httpService.sendGetWithOffset(listGet.size())));
        List<Todo> listGetMinusListGetWithOffsetMoreThenElemsCnt = firstListMinusSecond(listGet,
                toList(httpService.sendGetWithOffset(listGet.size() + 1)));

        log.info("Step3: сравниваем размеры списков");
        assertAll(
                () -> assertEquals(1, listGetMinusListGetWithOffset1.size()),
                () -> assertEquals(listGet.size(), listGetMinusListGetWithOffsetEqualsElemsCnt.size()),
                () -> assertEquals(listGet.size(), listGetMinusListGetWithOffsetMoreThenElemsCnt.size())
        );

        log.info("Post-condition: удаляем добавленные сущности");
        removeById(id1);
        removeById(id2);
    }

    @DisplayName("c параметром limit вернуть todo не более limit")
    @Test
    public void shouldHaveCorrectNotMoreLimitAfterGetRequestTest() {
        log.info("Precondition: Вставляем 2 сущности");
        long id1 = generateId();
        long id2 = generateId();

        HttpServiceImpl httpService = new HttpServiceImpl();
        httpService.sendPost(new Todo(id1, "get_limit_1", true))
                .then().statusCode(SUCCESS_POST);

        httpService.sendPost(new Todo(id2, "get_limit_2", false))
                .then().statusCode(SUCCESS_POST);

        log.info("Step2: получаем список из всех элементов и списки с limit равным 0, 1 " +
                "и количеством элементов в исходном списке ");
        List<Todo> listGet = toList(httpService.sendGet());
        List<Todo> listGetWithLimit0 = toList(httpService.sendGetWithLimit(0));
        List<Todo> listGetWithLimit1 = toList(httpService.sendGetWithLimit(1));
        List<Todo> listGetWithLimitListGet = toList(httpService.sendGetWithLimit(listGet.size()));

        log.info("Step3: сравниваем размеры соответсвуюших списков из Step2");
        assertAll(
                () -> assertEquals(0, listGetWithLimit0.size()),
                () -> assertEquals(1, listGetWithLimit1.size()),
                () -> assertEquals(listGet.size(), listGetWithLimitListGet.size())
        );

        log.info("Post-condition: удаляем добавленные сущности");
        removeById(id1);
        removeById(id2);
    }

    @DisplayName("корректно работать одновременно с limit и offset")
    @Test
    public void shouldHaveCorrectWithLimitAndOffsetTest() {
        log.info("Precondition: Вставляем 2 сущности");
        long id1 = generateId();
        long id2 = generateId();

        HttpServiceImpl httpService = new HttpServiceImpl();
        httpService.sendPost(new Todo(id1, "get_offset_limit_1", true))
                .then().statusCode(SUCCESS_POST);
        httpService.sendPost(new Todo(id2, "get_offset_limit_2", false))
                .then().statusCode(SUCCESS_POST);

        log.info("Step2: получаем список из всех элементов");
        List<Todo> listGet = toList(httpService.sendGet());

        log.info("Step3: получаем разность спиков исходного и с offset = 0,1,0 и limit=0,1 и размером исходного списка");
        List<Todo> listGetMinusListGetWithOffset0Limit0 = firstListMinusSecond(listGet,
                toList(httpService.sendGetWithOffsetAndLimit(0, 0)));
        List<Todo> listGetMinusListGetWithOffset0Limit1 = firstListMinusSecond(listGet,
                toList(httpService.sendGetWithOffsetAndLimit(0, 1)));
        List<Todo> listGetMinusListGetWithOffset1Limit0 = firstListMinusSecond(listGet,
                toList(httpService.sendGetWithOffsetAndLimit(1, 0)));
        List<Todo> listGetMinusListGetWithOffset1Limit1 = firstListMinusSecond(listGet,
                toList(httpService.sendGetWithOffsetAndLimit(1, 1)));
        List<Todo> listGetWithOffset0LimitListGet =
                toList(httpService.sendGetWithOffsetAndLimit(0, listGet.size()));

        log.info("Step4: сравниваем размеры соответсвуюших списков из Step3");
        assertAll(
                () -> assertEquals(listGet.size(), listGetMinusListGetWithOffset0Limit0.size()),
                () -> assertEquals(listGet.size() - 1, listGetMinusListGetWithOffset0Limit1.size()),
                () -> assertEquals(listGet.size(), listGetMinusListGetWithOffset1Limit0.size()),
                () -> assertEquals(listGet.size() - 1, listGetMinusListGetWithOffset1Limit1.size()),
                () -> assertEquals(listGet.size(), listGetWithOffset0LimitListGet.size())
        );

        log.info("Post-condition: удаляем добавленные сущности");
        removeById(id1);
        removeById(id2);
    }

    private List<Todo> toList(Response response) {
        return response.body().jsonPath().getList(".", Todo.class);
    }

    private List<Todo> firstListMinusSecond(List<Todo> firstList, List<Todo> secondList) {
        return firstList.stream()
                .filter(element -> !secondList.contains(element))
                .collect(Collectors.toList());
    }
}