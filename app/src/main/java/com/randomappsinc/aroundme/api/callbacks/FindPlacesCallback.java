package com.randomappsinc.aroundme.api.callbacks;

import android.support.annotation.NonNull;

import com.randomappsinc.aroundme.api.ApiConstants;
import com.randomappsinc.aroundme.api.RestClient;
import com.randomappsinc.aroundme.api.models.PlaceResults;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindPlacesCallback implements Callback<PlaceResults> {

    @Override
    public void onResponse(@NonNull Call<PlaceResults> call, @NonNull Response<PlaceResults> response) {
        if (response.code() == ApiConstants.HTTP_STATUS_OK) {
            RestClient.getInstance().processPlaces(response.body().getPlaces());
        } else if (response.code() == ApiConstants.HTTP_STATUS_UNAUTHORIZED) {
            RestClient.getInstance().refreshToken();
        }
        // TODO: Process failure here
    }

    @Override
    public void onFailure(@NonNull Call<PlaceResults> call, @NonNull Throwable t) {
        // TODO: Deal with the place search failing case
    }
}
