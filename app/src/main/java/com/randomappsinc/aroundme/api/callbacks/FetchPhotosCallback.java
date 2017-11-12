package com.randomappsinc.aroundme.api.callbacks;

import android.support.annotation.NonNull;

import com.randomappsinc.aroundme.api.ApiConstants;
import com.randomappsinc.aroundme.api.RestClient;
import com.randomappsinc.aroundme.api.models.BusinessInfoFetchError;
import com.randomappsinc.aroundme.api.models.PlacePhotos;
import com.randomappsinc.aroundme.persistence.DatabaseManager;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class FetchPhotosCallback implements Callback<PlacePhotos> {

    @Override
    public void onResponse(@NonNull Call<PlacePhotos> call, @NonNull Response<PlacePhotos> response) {
        if (response.code() == ApiConstants.HTTP_STATUS_OK) {
            // Update place object in DB since the call returns the entire place object anyways
            DatabaseManager.get().getPlacesDBManager().updateFavorite(response.body().getPlace());

            RestClient.getInstance().processPhotos(response.body().getPhotoUrls());
        } else if (response.code() == ApiConstants.HTTP_STATUS_UNAUTHORIZED) {
            RestClient.getInstance().refreshToken();
        } else if (response.code() == ApiConstants.HTTP_STATUS_FORBIDDEN) {
            Converter<ResponseBody, BusinessInfoFetchError> errorConverter =
                    RestClient.getInstance().getRetrofitInstance()
                            .responseBodyConverter(BusinessInfoFetchError.class, new Annotation[0]);
            try {
                BusinessInfoFetchError error = errorConverter.convert(response.errorBody());
                if (error.getCode().equals(ApiConstants.BUSINESS_UNAVAILABLE)) {
                    RestClient.getInstance().processPhotos(new ArrayList<String>());
                }
            } catch (IOException ignored) {}
        }
        // TODO: Process failure here
    }

    @Override
    public void onFailure(@NonNull Call<PlacePhotos> call, @NonNull Throwable t) {
        // TODO: Process failure here
    }
}
