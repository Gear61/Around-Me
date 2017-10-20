package com.randomappsinc.aroundme.api.callbacks;

import com.randomappsinc.aroundme.api.ApiConstants;
import com.randomappsinc.aroundme.api.RestClient;
import com.randomappsinc.aroundme.api.models.TokenResponse;
import com.randomappsinc.aroundme.persistence.PreferencesManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchTokenCallback implements Callback<TokenResponse> {

    @Override
    public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
        if (response.code() == ApiConstants.HTTP_STATUS_OK &&
                response.body() != null &&
                response.body().getAccessToken() != null) {
            PreferencesManager.get().setBearerToken(response.body().getAccessToken());
        } else {
            RestClient.getInstance().refreshToken();
        }
    }

    @Override
    public void onFailure(Call<TokenResponse> call, Throwable t) {
        RestClient.getInstance().refreshToken();
    }
}
