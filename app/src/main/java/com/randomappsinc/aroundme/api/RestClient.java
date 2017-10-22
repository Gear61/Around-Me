package com.randomappsinc.aroundme.api;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;

import com.randomappsinc.aroundme.api.callbacks.FetchTokenCallback;
import com.randomappsinc.aroundme.api.callbacks.FindPlacesCallback;
import com.randomappsinc.aroundme.api.models.SearchResults;
import com.randomappsinc.aroundme.models.Place;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    public interface PlacesListener {
        void onPlacesFetched(List<Place> places);
    }

    private static final PlacesListener DUMMY_PLACES_LISTENER = new PlacesListener() {
        @Override
        public void onPlacesFetched(List<Place> places) {}
    };

    private static RestClient mInstance;

    private YelpService mYelpService;
    private Handler mHandler;

    // Places
    @NonNull private PlacesListener mPlacesListener = DUMMY_PLACES_LISTENER;
    private Call<SearchResults> mCurrentFetchPlacesCall;

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

        HandlerThread backgroundThread = new HandlerThread("");
        backgroundThread.start();
        mHandler = new Handler(backgroundThread.getLooper());
    }

    public void refreshToken() {
        mYelpService.fetchToken(YelpToken.CLIENT_ID, YelpToken.CLIENT_SECRET, ApiConstants.GRANT_TYPE)
                .enqueue(new FetchTokenCallback());
    }

    public void fetchPlaces(final String searchTerm, final String location) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentFetchPlacesCall != null) {
                    mCurrentFetchPlacesCall.cancel();
                }
                mCurrentFetchPlacesCall = mYelpService.findPlaces(
                        searchTerm,
                        location,
                        ApiConstants.DEFAULT_NUM_PLACES,
                        ApiConstants.DISTANCE_SORT);
                mCurrentFetchPlacesCall.enqueue(new FindPlacesCallback());
            }
        });
    }

    public void registerPlacesListener(PlacesListener placesListener) {
        mPlacesListener = placesListener;
    }

    public void unregisterPlacesListener() {
        mPlacesListener = DUMMY_PLACES_LISTENER;
    }

    public void processPlaces(List<Place> places) {
        mPlacesListener.onPlacesFetched(places);
    }

    public void cancelPlacesFetch() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentFetchPlacesCall != null) {
                    mCurrentFetchPlacesCall.cancel();
                }
            }
        });
    }
}
