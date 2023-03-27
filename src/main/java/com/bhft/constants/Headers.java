package com.bhft.constants;

import com.bhft.utils.Props;
import io.restassured.http.*;

import java.util.Base64;

public class Headers {
    private static final String AUTHORIZATION_VALUE = "Basic " + Base64.getEncoder()
            .encodeToString((Props.getProperty("user") + ":" + Props.getProperty("password")).getBytes());
    public static final Header BASIC_AUTHORIZATION = new Header("Authorization", AUTHORIZATION_VALUE);
}
