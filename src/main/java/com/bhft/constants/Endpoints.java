package com.bhft.constants;

import com.bhft.utils.Props;

public class Endpoints {
    public final static String BASE_URL = Props.getProperty("base_url");
    public final static String GET_ENDPOINT = "/todos";
    public final static String POST_ENDPOINT = "/todos";
    public final static String PUT_ENDPOINT = "/todos/{id}";
    public final static String DELETE_ENDPOINT = "/todos/{id}";
}