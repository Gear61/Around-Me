package com.randomappsinc.aroundme.api;

import android.os.Handler;
import android.os.HandlerThread;
import androidx.annotation.NonNull;

import com.randomappsinc.aroundme.api.callbacks.FetchPhotosCallback;
import com.randomappsinc.aroundme.api.callbacks.FetchReviewsCallback;
import com.randomappsinc.aroundme.api.callbacks.FindEventsCallback;
import com.randomappsinc.aroundme.api.callbacks.FindPlacesCallback;
import com.randomappsinc.aroundme.api.models.EventSearchResults;
import com.randomappsinc.aroundme.api.models.PlacePhotos;
import com.randomappsinc.aroundme.api.models.PlaceReviewResults;
import com.randomappsinc.aroundme.api.models.PlaceSearchResults;
import com.randomappsinc.aroundme.models.Event;
import com.randomappsinc.aroundme.models.Filter;
import com.randomappsinc.aroundme.models.Place;
import com.randomappsinc.aroundme.models.PlaceReview;
import com.randomappsinc.aroundme.persistence.PreferencesManager;

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
        void onReviewsFetched(List<PlaceReview> photos);
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
        public void onReviewsFetched(List<PlaceReview> reviews) {}
    };

    private static final EventsListener DUMMY_EVENTS_LISTENER = new EventsListener() {
        @Override
        public void onEventsFetched(List<Event> events) {}
    };

    private static RestClient instance;

    private Retrofit retrofit;
    private YelpService yelpService;
    private Handler handler;

    // Places
    @NonNull private PlacesListener placesListener = DUMMY_PLACES_LISTENER;
    private Call<PlaceSearchResults> currentFindPlacesCall;

    // Photos
    @NonNull private PhotosListener photosListener = DUMMY_PHOTOS_LISTENER;
    private Call<PlacePhotos> currentFetchPhotosCall;

    // Reviews
    @NonNull private ReviewsListener reviewsListener = DUMMY_REVIEWS_LISTENER;
    private Call<PlaceReviewResults> currentFetchReviewsCall;

    // Events
    @NonNull private EventsListener eventsListener = DUMMY_EVENTS_LISTENER;
    private Call<EventSearchResults> currentFindEventsCall;

    public static RestClient getInstance() {
        if (instance == null) {
            instance = new RestClient();
        }
        return instance;
    }

    private RestClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        yelpService = retrofit.create(YelpService.class);

        HandlerThread backgroundThread = new HandlerThread("");
        backgroundThread.start();
        handler = new Handler(backgroundThread.getLooper());
    }

    public Retrofit getRetrofitInstance() {
        return retrofit;
    }

    public void findPlaces(final String searchTerm, final String location) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (currentFindPlacesCall != null) {
                    currentFindPlacesCall.cancel();
                }
                Filter filter = PreferencesManager.get().getFilter();
                currentFindPlacesCall = yelpService.findPlaces(
                        searchTerm,
                        location,
                        ApiConstants.DEFAULT_NUM_PLACES,
                        filter.getSortType(),
                        true,
                        (int) filter.getRadius(),
                        filter.getPriceRangesString(),
                        filter.getAttributesString());
                currentFindPlacesCall.enqueue(new FindPlacesCallback());
            }
        });
    }

    public void registerPlacesListener(PlacesListener placesListener) {
        this.placesListener = placesListener;
    }

    public void unregisterPlacesListener() {
        placesListener = DUMMY_PLACES_LISTENER;
    }

    public void processPlaces(List<Place> places) {
        placesListener.onPlacesFetched(places);
    }

    public void cancelPlacesFetch() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (currentFindPlacesCall != null) {
                    currentFindPlacesCall.cancel();
                }
            }
        });
    }

    public void fetchPlacePhotos(final Place place) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (currentFetchPhotosCall != null) {
                    currentFetchPhotosCall.cancel();
                }
                currentFetchPhotosCall = yelpService.fetchPlacePhotos(place.getId());
                currentFetchPhotosCall.enqueue(new FetchPhotosCallback());
            }
        });
    }

    public void registerPhotosListener(PhotosListener photosListener) {
        this.photosListener = photosListener;
    }

    public void unregisterPhotosListener() {
        photosListener = DUMMY_PHOTOS_LISTENER;
    }

    public void processPhotos(List<String> photoUrls) {
        photosListener.onPhotosFetched(photoUrls);
    }

    public void cancelPhotosFetch() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (currentFetchPhotosCall != null) {
                    currentFetchPhotosCall.cancel();
                }
            }
        });
    }

    public void fetchPlaceReviews(final Place place) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (currentFetchReviewsCall != null) {
                    currentFetchReviewsCall.cancel();
                }
                currentFetchReviewsCall = yelpService.fetchPlaceReviews(place.getId());
                currentFetchReviewsCall.enqueue(new FetchReviewsCallback());
            }
        });
    }

    public void registerReviewsListener(ReviewsListener reviewsListener) {
        this.reviewsListener = reviewsListener;
    }

    public void unregisterReviewsListener() {
        reviewsListener = DUMMY_REVIEWS_LISTENER;
    }

    public void processReviews(List<PlaceReview> reviews) {
        reviewsListener.onReviewsFetched(reviews);
    }

    public void cancelReviewsFetch() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (currentFetchReviewsCall != null) {
                    currentFetchReviewsCall.cancel();
                }
            }
        });
    }

    public void findEvents(final String location) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (currentFindEventsCall != null) {
                    currentFindEventsCall.cancel();
                }

                long currentUnixTime = System.currentTimeMillis() / 1000L;
                currentFindEventsCall = yelpService.findEvents(
                        location,
                        currentUnixTime,
                        ApiConstants.DEFAULT_NUM_EVENTS,
                        ApiConstants.TIME_START_SORT,
                        ApiConstants.ASC_SORT);
                currentFindEventsCall.enqueue(new FindEventsCallback());
            }
        });
    }

    public void registerEventsListener(EventsListener eventsListener) {
        this.eventsListener = eventsListener;
    }

    public void unregisterEventsListener() {
        eventsListener = DUMMY_EVENTS_LISTENER;
    }

    public void processEvents(List<Event> events) {
        eventsListener.onEventsFetched(events);
    }

    public void cancelEventsFetch() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (currentFindEventsCall != null) {
                    currentFindEventsCall.cancel();
                }
            }
        });
    }
}
