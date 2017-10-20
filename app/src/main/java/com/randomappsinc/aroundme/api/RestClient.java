package com.randomappsinc.aroundme.api;

import com.randomappsinc.aroundme.api.callbacks.FetchTokenCallback;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private static RestClient mInstance;
    private YelpService mYelpService;

    public static RestClient getInstance() {
        if (mInstance == null) {
            mInstance = new RestClient();
        }
        return mInstance;
    }

    private RestClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mYelpService = retrofit.create(YelpService.class);
    }

    public void refreshToken() {
        mYelpService.fetchToken(YelpToken.CLIENT_ID, YelpToken.CLIENT_SECRET, ApiConstants.GRANT_TYPE)
                .enqueue(new FetchTokenCallback());
    }
}
