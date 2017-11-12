package com.randomappsinc.aroundme.api;

public class ApiConstants {

    static final String GRANT_TYPE = "client_credentials";

    static final String BASE_URL = "https://api.yelp.com";

    static final String AUTHORIZATION = "Authorization";
    static final String BEARER_PREFIX = "Bearer ";

    public static final int HTTP_STATUS_OK = 200;
    public static final int HTTP_STATUS_UNAUTHORIZED = 401;
    public static final int HTTP_STATUS_FORBIDDEN = 403;

    static final int DEFAULT_NUM_PLACES = 20;
    static final String BEST_MATCH_SORT = "best_match";

    static final int DEFAULT_NUM_EVENTS = 20;
    static final String TIME_START_SORT = "time_start";
    static final String ASC_SORT = "asc";

    // Yelp error codes
    public static final String BUSINESS_UNAVAILABLE = "BUSINESS_UNAVAILABLE";
}
