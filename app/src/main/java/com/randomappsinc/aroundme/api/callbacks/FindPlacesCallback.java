package com.randomappsinc.aroundme.api.callbacks;

import androidx.annotation.NonNull;

import com.randomappsinc.aroundme.api.ApiConstants;
import com.randomappsinc.aroundme.api.RestClient;
import com.randomappsinc.aroundme.api.models.PlaceSearchResults;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindPlacesCallback implements Callback<PlaceSearchResults> {

    @Override
    public void onResponse(@NonNull Call<PlaceSearchResults> call, @NonNull Response<PlaceSearchResults> response) {
        if (response.code() == ApiConstants.HTTP_STATUS_OK) {
            RestClient.getInstance().processPlaces(response.body().getPlaces());
        }
        // TODO: Process failure here
    }

    @Override
    public void onFailure(@NonNull Call<PlaceSearchResults> call, @NonNull Throwable t) {
        // TODO: Deal with the place search failing case
    }
}
