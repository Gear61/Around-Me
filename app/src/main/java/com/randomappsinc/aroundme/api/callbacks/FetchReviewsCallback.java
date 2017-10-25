package com.randomappsinc.aroundme.api.callbacks;

import android.support.annotation.NonNull;

import com.randomappsinc.aroundme.api.ApiConstants;
import com.randomappsinc.aroundme.api.RestClient;
import com.randomappsinc.aroundme.api.models.PlaceReviews;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchReviewsCallback implements Callback<PlaceReviews> {

    @Override
    public void onResponse(@NonNull Call<PlaceReviews> call, @NonNull Response<PlaceReviews> response) {
        if (response.code() == ApiConstants.HTTP_STATUS_OK) {
            RestClient.getInstance().processReviews(response.body().getReviews());
        } else if (response.code() == ApiConstants.HTTP_STATUS_UNAUTHORIZED) {
            RestClient.getInstance().refreshToken();
        }
    }

    @Override
    public void onFailure(@NonNull Call<PlaceReviews> call, @NonNull Throwable t) {
        // TODO: Handle failure here
    }
}