package com.randomappsinc.aroundme.api.callbacks;

import android.support.annotation.NonNull;

import com.randomappsinc.aroundme.api.ApiConstants;
import com.randomappsinc.aroundme.api.RestClient;
import com.randomappsinc.aroundme.api.models.EventResults;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindEventsCallback implements Callback<EventResults> {

    @Override
    public void onResponse(@NonNull Call<EventResults> call, @NonNull Response<EventResults> response) {
        if (response.code() == ApiConstants.HTTP_STATUS_OK) {
            RestClient.getInstance().processEvents(response.body().getEvents());
        } else if (response.code() == ApiConstants.HTTP_STATUS_UNAUTHORIZED) {
            RestClient.getInstance().refreshToken();
        }
        // TODO: Process failure here
    }

    @Override
    public void onFailure(@NonNull Call<EventResults> call, @NonNull Throwable t) {
        // TODO: Process failure here
    }
}
