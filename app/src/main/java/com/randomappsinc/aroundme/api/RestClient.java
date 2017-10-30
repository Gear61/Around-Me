package com.randomappsinc.aroundme.api;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;

import com.randomappsinc.aroundme.api.callbacks.FetchPhotosCallback;
import com.randomappsinc.aroundme.api.callbacks.FetchReviewsCallback;
import com.randomappsinc.aroundme.api.callbacks.FetchTokenCallback;
import com.randomappsinc.aroundme.api.callbacks.FindEventsCallback;
import com.randomappsinc.aroundme.api.callbacks.FindPlacesCallback;
import com.randomappsinc.aroundme.api.models.EventResults;
import com.randomappsinc.aroundme.api.models.PlacePhotos;
import com.randomappsinc.aroundme.api.models.PlaceResults;
import com.randomappsinc.aroundme.api.models.PlaceReviews;
import com.randomappsinc.aroundme.models.Event;
import com.randomappsinc.aroundme.models.Place;
import com.randomappsinc.aroundme.models.Review;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    public interface PlacesListener {
        void onPlacesFetched(List<Place> places);
    }

    public interface PhotosListener {
        void onPhotosFetched(List<String> photos);
    }

    public interface ReviewsListener {
        void onReviewsFetched(List<Review> photos);
    }

    public interface EventsListener {
        void onEventsFetched(List<Event> events);
    }

    private static final PlacesListener DUMMY_PLACES_LISTENER = new PlacesListener() {
        @Override
        public void onPlacesFetched(List<Place> places) {}
    };

    private static final PhotosListener DUMMY_PHOTOS_LISTENER = new PhotosListener() {
        @Override
        public void onPhotosFetched(List<String> photos) {}
    };

    private static final ReviewsListener DUMMY_REVIEWS_LISTENER = new ReviewsListener() {
        @Override
        public void onReviewsFetched(List<Review> reviews) {}
    };

    private static final EventsListener DUMMY_EVENTS_LISTENER = new EventsListener() {
        @Override
        public void onEventsFetched(List<Event> events) {}
    };

    private static RestClient mInstance;

    private YelpService mYelpService;
    private Handler mHandler;

    // Places
    @NonNull private PlacesListener mPlacesListener = DUMMY_PLACES_LISTENER;
    private Call<PlaceResults> mCurrentFindPlacesCall;

    // Photos
    @NonNull private PhotosListener mPhotosListener = DUMMY_PHOTOS_LISTENER;
    private Call<PlacePhotos> mCurrentFetchPhotosCall;

    // Reviews
    @NonNull private ReviewsListener mReviewsListener = DUMMY_REVIEWS_LISTENER;
    private Call<PlaceReviews> mCurrentFetchReviewsCall;

    // Events
    @NonNull private EventsListener mEventsListener = DUMMY_EVENTS_LISTENER;
    private Call<EventResults> mCurrentFindEventsCall;

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

    public void findPlaces(final String searchTerm, final String location) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentFindPlacesCall != null) {
                    mCurrentFindPlacesCall.cancel();
                }
                mCurrentFindPlacesCall = mYelpService.findPlaces(
                        searchTerm,
                        location,
                        ApiConstants.DEFAULT_NUM_PLACES,
                        ApiConstants.DISTANCE_SORT);
                mCurrentFindPlacesCall.enqueue(new FindPlacesCallback());
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
                if (mCurrentFindPlacesCall != null) {
                    mCurrentFindPlacesCall.cancel();
                }
            }
        });
    }

    public void fetchPlacePhotos(final Place place) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentFetchPhotosCall != null) {
                    mCurrentFetchPhotosCall.cancel();
                }
                mCurrentFetchPhotosCall = mYelpService.fetchPlacePhotos(place.getId());
                mCurrentFetchPhotosCall.enqueue(new FetchPhotosCallback());
            }
        });
    }

    public void registerPhotosListener(PhotosListener photosListener) {
        mPhotosListener = photosListener;
    }

    public void unregisterPhotosListener() {
        mPhotosListener = DUMMY_PHOTOS_LISTENER;
    }

    public void processPhotos(List<String> photoUrls) {
        mPhotosListener.onPhotosFetched(photoUrls);
    }

    public void cancelPhotosFetch() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentFetchPhotosCall != null) {
                    mCurrentFetchPhotosCall.cancel();
                }
            }
        });
    }

    public void fetchPlaceReviews(final Place place) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentFetchReviewsCall != null) {
                    mCurrentFetchReviewsCall.cancel();
                }
                mCurrentFetchReviewsCall = mYelpService.fetchPlaceReviews(place.getId());
                mCurrentFetchReviewsCall.enqueue(new FetchReviewsCallback());
            }
        });
    }

    public void registerReviewsListener(ReviewsListener reviewsListener) {
        mReviewsListener = reviewsListener;
    }

    public void unregisterReviewsListener() {
        mReviewsListener = DUMMY_REVIEWS_LISTENER;
    }

    public void processReviews(List<Review> reviews) {
        mReviewsListener.onReviewsFetched(reviews);
    }

    public void cancelReviewsFetch() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentFetchReviewsCall != null) {
                    mCurrentFetchReviewsCall.cancel();
                }
            }
        });
    }

    public void findEvents(final String location) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentFindEventsCall != null) {
                    mCurrentFindEventsCall.cancel();
                }

                long currentUnixTime = System.currentTimeMillis() / 1000L;
                mCurrentFindEventsCall = mYelpService.findEvents(
                        location,
                        currentUnixTime,
                        ApiConstants.DEFAULT_NUM_EVENTS,
                        ApiConstants.TIME_START_SORT,
                        ApiConstants.ASC_SORT);
                mCurrentFindEventsCall.enqueue(new FindEventsCallback());
            }
        });
    }

    public void registerEventsListener(EventsListener eventsListener) {
        mEventsListener = eventsListener;
    }

    public void unregisterEventsListener() {
        mEventsListener = DUMMY_EVENTS_LISTENER;
    }

    public void processEvents(List<Event> events) {
        mEventsListener.onEventsFetched(events);
    }

    public void cancelEventsFetch() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentFindEventsCall != null) {
                    mCurrentFindEventsCall.cancel();
                }
            }
        });
    }
}
