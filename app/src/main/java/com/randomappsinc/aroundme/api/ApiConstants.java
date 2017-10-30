package com.randomappsinc.aroundme.api;

public class ApiConstants {

    static final String GRANT_TYPE = "client_credentials";

    static final String BASE_URL = "https://api.yelp.com";

    static final String AUTHORIZATION = "Authorization";
    static final String BEARER_PREFIX = "Bearer ";

    public static final int HTTP_STATUS_OK = 200;
    public static final int HTTP_STATUS_UNAUTHORIZED = 401;

    static final int DEFAULT_NUM_PLACES = 20;
    static final String DISTANCE_SORT = "distance";

    static final int DEFAULT_NUM_EVENTS = 20;
}
