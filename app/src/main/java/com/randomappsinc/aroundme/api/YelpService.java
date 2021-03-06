package com.randomappsinc.aroundme.api;

import com.randomappsinc.aroundme.api.models.EventSearchResults;
import com.randomappsinc.aroundme.api.models.PlacePhotos;
import com.randomappsinc.aroundme.api.models.PlaceReviewResults;
import com.randomappsinc.aroundme.api.models.PlaceSearchResults;
import com.randomappsinc.aroundme.constants.SortType;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface YelpService {

    @GET("v3/businesses/search")
    Call<PlaceSearchResults> findPlaces(@Query("term") String term,
                                        @Query("location") String location,
                                        @Query("limit") int limit,
                                        @Query("sort_by") @SortType String sortBy,
                                        @Query("open_now") boolean openNow,
                                        @Query("radius") int radius,
                                        @Query("price") String priceRanges,
                                        @Query("attributes") String attributes);

    @GET("v3/businesses/{id}")
    Call<PlacePhotos> fetchPlacePhotos(@Path("id") String placeId);

    @GET("v3/businesses/{id}/reviews")
    Call<PlaceReviewResults> fetchPlaceReviews(@Path("id") String placeId);

    @GET("/v3/events")
    Call<EventSearchResults> findEvents(@Query("location") String location,
                                        @Query("start_date") long startDate,
                                        @Query("limit") int limit,
                                        @Query("sort_on") String sortOn,
                                        @Query("sort_by") String sortBy);
}
