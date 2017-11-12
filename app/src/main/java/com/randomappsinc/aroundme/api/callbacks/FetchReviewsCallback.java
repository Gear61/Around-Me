package com.randomappsinc.aroundme.api.callbacks;

import android.support.annotation.NonNull;

import com.randomappsinc.aroundme.api.ApiConstants;
import com.randomappsinc.aroundme.api.RestClient;
import com.randomappsinc.aroundme.api.models.BusinessInfoFetchError;
import com.randomappsinc.aroundme.api.models.PlaceReviewResults;
import com.randomappsinc.aroundme.models.PlaceReview;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class FetchReviewsCallback implements Callback<PlaceReviewResults> {

    @Override
    public void onResponse(@NonNull Call<PlaceReviewResults> call, @NonNull Response<PlaceReviewResults> response) {
        if (response.code() == ApiConstants.HTTP_STATUS_OK) {
            RestClient.getInstance().processReviews(response.body().getReviews());
        } else if (response.code() == ApiConstants.HTTP_STATUS_UNAUTHORIZED) {
            RestClient.getInstance().refreshToken();
        } else if (response.code() == ApiConstants.HTTP_STATUS_FORBIDDEN) {
            Converter<ResponseBody, BusinessInfoFetchError> errorConverter =
                    RestClient.getInstance().getRetrofitInstance()
                            .responseBodyConverter(BusinessInfoFetchError.class, new Annotation[0]);
            try {
                BusinessInfoFetchError error = errorConverter.convert(response.errorBody());
                if (error.getCode().equals(ApiConstants.BUSINESS_UNAVAILABLE)) {
                    RestClient.getInstance().processReviews(new ArrayList<PlaceReview>());
                }
            } catch (IOException ignored) {}
        }
    }

    @Override
    public void onFailure(@NonNull Call<PlaceReviewResults> call, @NonNull Throwable t) {
        // TODO: Handle failure here
    }
}
