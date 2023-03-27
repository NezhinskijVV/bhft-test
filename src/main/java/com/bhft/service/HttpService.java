package com.bhft.service;

import com.bhft.domain.Todo;
import io.restassured.response.Response;

public interface HttpService {

    Response sendGet();
    Response sendGetWithOffset(int offset);
    Response sendGetWithLimit(int limit);
    Response sendGetWithOffsetAndLimit(int offset, int limit);

    Response sendPost(Todo todo);

    Response sendPut(Todo todo);

    Response sendDelete(long id);
}
