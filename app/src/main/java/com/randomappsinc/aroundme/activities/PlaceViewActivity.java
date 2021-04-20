package com.randomappsinc.aroundme.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.adapters.PlacePhotosAdapter;
import com.randomappsinc.aroundme.adapters.PlaceReviewsAdapter;
import com.randomappsinc.aroundme.api.RestClient;
import com.randomappsinc.aroundme.models.Place;
import com.randomappsinc.aroundme.models.PlaceReview;
import com.randomappsinc.aroundme.persistence.DatabaseManager;
import com.randomappsinc.aroundme.utils.UIUtils;
import com.randomappsinc.aroundme.views.PlaceInfoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlaceViewActivity extends StandardActivity implements RestClient.PhotosListener,
        RestClient.ReviewsListener, PlaceReviewsAdapter.Listener, PlacePhotosAdapter.Listener, OnMapReadyCallback {

    public static final String PLACE_KEY = "place";
    public static final String FROM_FAVORITES = "fromFavorites";

    @BindView(R.id.favorite_toggle) TextView favoriteToggle;
    @BindView(R.id.place_map) MapView placeMap;
    @BindView(R.id.place_info_parent) View placeInfo;
    @BindView(R.id.photos_stub) View photosStub;
    @BindView(R.id.place_photos) RecyclerView photosList;
    @BindView(R.id.reviews_stub) View reviewsStub;
    @BindView(R.id.reviews_container) LinearLayout reviewsContainer;

    @BindColor(R.color.light_red) int heartRed;
    @BindColor(R.color.dark_gray) int darkGray;

    private Place place;
    private PlaceInfoView placeInfoView;
    private RestClient restClient;
    private PlacePhotosAdapter photosAdapter;
    private PlaceReviewsAdapter reviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_view);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        place = getIntent().getParcelableExtra(PLACE_KEY);
        setTitle(place.getName());

        placeMap.onCreate(savedInstanceState);
        placeMap.getMapAsync(this);

        // It's possible you're finding something in search that you've already favorited
        place.setIsFavorited(DatabaseManager.get().getPlacesDBManager().isPlaceFavorited(place));

        favoriteToggle.setText(place.isFavorited() ? R.string.heart_filled_icon : R.string.heart_icon);
        favoriteToggle.setTextColor(place.isFavorited() ? heartRed : darkGray);

        photosAdapter = new PlacePhotosAdapter(this, this);
        photosList.setAdapter(photosAdapter);
        reviewsAdapter = new PlaceReviewsAdapter(this);

        restClient = RestClient.getInstance();
        restClient.registerPhotosListener(this);
        restClient.fetchPlacePhotos(place);
        restClient.registerReviewsListener(this);
        restClient.fetchPlaceReviews(place);

        placeInfoView = new PlaceInfoView(
                placeInfo,
                new IconDrawable(this, IoniconsIcons.ion_location).colorRes(R.color.dark_gray));
        boolean fromFavorites = getIntent().getBooleanExtra(FROM_FAVORITES, false);
        placeInfoView.loadPlace(place, fromFavorites);
    }

    @OnClick(R.id.favorite_toggle)
    public void onFavoriteClick() {
        place.toggleFavorite();
        UIUtils.animateFavoriteToggle(favoriteToggle, place.isFavorited());
        if (place.isFavorited()) {
            DatabaseManager.get().getPlacesDBManager().addFavorite(place);
        } else {
            DatabaseManager.get().getPlacesDBManager().removeFavorite(place);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.setOnMapClickListener(mMapClickListener);

        LatLng place = new LatLng(this.place.getLatitude(), this.place.getLongitude());
        googleMap.addMarker(new MarkerOptions()
                .position(place)
                .title(this.place.getName()));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(place)
                .zoom(16)
                .bearing(0)
                .tilt(0)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private final GoogleMap.OnMapClickListener mMapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            String mapUri = "google.navigation:q=" + place.getAddress() + " " + place.getName();
            startActivity(Intent.createChooser(
                    new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(mapUri)),
                    getString(R.string.navigate_with)));
        }
    };

    @OnClick(R.id.place_thumbnail)
    public void onThumbnailClicked() {
        if (TextUtils.isEmpty(place.getImageUrl())) {
            return;
        }

        Intent intent = new Intent(this, PictureFullViewActivity.class);
        ArrayList<String> imageUrl = new ArrayList<>();
        imageUrl.add(place.getImageUrl());
        intent.putStringArrayListExtra(PictureFullViewActivity.IMAGE_URLS_KEY, imageUrl);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @OnClick(R.id.call_button)
    public void callPlace() {
        String phoneUri = "tel:" + place.getPhoneNumber();
        startActivity(Intent.createChooser(
                new Intent(Intent.ACTION_DIAL, Uri.parse(phoneUri)),
                getString(R.string.call_with)));
    }

    @OnClick(R.id.share_button)
    public void sharePlace() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(place.getUrl())
                .getIntent();
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(shareIntent);
        }
    }

    @Override
    public void onPhotosFetched(List<String> photos) {
        photosStub.setVisibility(View.INVISIBLE);
        photosAdapter.setPhotoUrls(photos);
        photosList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onReviewsFetched(List<PlaceReview> reviews) {
        reviewsStub.setVisibility(View.GONE);
        reviewsAdapter.setReviews(reviews, reviewsContainer, this);
    }

    @Override
    public void onReviewClicked(PlaceReview review) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl()));
        startActivity(intent);
    }

    @Override
    public void onPhotoClicked(ArrayList<String> imageUrls, int position) {
        Intent intent = new Intent(this, PictureFullViewActivity.class);
        intent.putStringArrayListExtra(PictureFullViewActivity.IMAGE_URLS_KEY, imageUrls);
        intent.putExtra(PictureFullViewActivity.POSITION_KEY, position);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        placeMap.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        placeMap.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        placeMap.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        placeMap.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        placeMap.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        placeMap.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        placeMap.onDestroy();

        // Stop listening for photo fetch results
        restClient.cancelPhotosFetch();
        restClient.unregisterPhotosListener();

        // Stop listening for review fetch results
        restClient.cancelReviewsFetch();
        restClient.unregisterReviewsListener();
    }
}
