package com.randomappsinc.aroundme.api;

import com.randomappsinc.aroundme.api.models.EventSearchResults;
import com.randomappsinc.aroundme.api.models.PlacePhotos;
import com.randomappsinc.aroundme.api.models.PlaceReviews;
import com.randomappsinc.aroundme.api.models.PlaceSearchResults;
import com.randomappsinc.aroundme.api.models.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface YelpService {

    @FormUrlEncoded
    @POST("oauth2/token")
    Call<TokenResponse> fetchToken(@Field("client_id") String clientId,
                                   @Field("client_secret") String clientSecret,
                                   @Field("grant_type") String last);

    @GET("v3/businesses/search")
    Call<PlaceSearchResults> findPlaces(@Query("term") String term,
                                        @Query("location") String location,
                                        @Query("limit") int limit,
                                        @Query("sort_by") String sortBy,
                                        @Query("open_now") boolean openNow);

    @GET("v3/businesses/{id}")
    Call<PlacePhotos> fetchPlacePhotos(@Path("id") String placeId);

    @GET("v3/businesses/{id}/reviews")
    Call<PlaceReviews> fetchPlaceReviews(@Path("id") String placeId);

    @GET("/v3/events")
    Call<EventSearchResults> findEvents(@Query("location") String location,
                                        @Query("start_date") long startDate,
                                        @Query("limit") int limit,
                                        @Query("sort_on") String sortOn,
                                        @Query("sort_by") String sortBy);
}
