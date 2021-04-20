package com.randomappsinc.aroundme.api.callbacks;

import androidx.annotation.NonNull;

import com.randomappsinc.aroundme.api.ApiConstants;
import com.randomappsinc.aroundme.api.RestClient;
import com.randomappsinc.aroundme.api.models.EventSearchResults;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindEventsCallback implements Callback<EventSearchResults> {

    @Override
    public void onResponse(@NonNull Call<EventSearchResults> call, @NonNull Response<EventSearchResults> response) {
        if (response.code() == ApiConstants.HTTP_STATUS_OK) {
            RestClient.getInstance().processEvents(response.body().getEvents());
        }
        // TODO: Process failure here
    }

    @Override
    public void onFailure(@NonNull Call<EventSearchResults> call, @NonNull Throwable t) {
        // TODO: Process failure here
    }
}
