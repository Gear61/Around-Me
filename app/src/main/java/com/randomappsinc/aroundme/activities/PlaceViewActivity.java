package com.randomappsinc.aroundme.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    @BindView(R.id.favorite_toggle) TextView mFavoriteToggle;
    @BindView(R.id.place_map) MapView mPlaceMap;
    @BindView(R.id.place_info_parent) View mPlaceInfo;
    @BindView(R.id.photos_stub) View mPhotosStub;
    @BindView(R.id.place_photos) RecyclerView mPhotos;
    @BindView(R.id.reviews_stub) View mReviewsStub;
    @BindView(R.id.reviews_container) LinearLayout mReviewsContainer;

    @BindColor(R.color.light_red) int heartRed;
    @BindColor(R.color.dark_gray) int darkGray;

    private Place mPlace;
    private PlaceInfoView mPlaceInfoView;
    private RestClient mRestClient;
    private PlacePhotosAdapter mPhotosAdapter;
    private PlaceReviewsAdapter mReviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_view);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mPlace = getIntent().getParcelableExtra(PLACE_KEY);
        setTitle(mPlace.getName());

        mPlaceMap.onCreate(savedInstanceState);
        mPlaceMap.getMapAsync(this);

        // It's possible you're finding something in search that you've already favorited
        mPlace.setIsFavorited(DatabaseManager.get().getPlacesDBManager().isPlaceFavorited(mPlace));

        mFavoriteToggle.setText(mPlace.isFavorited() ? R.string.heart_filled_icon : R.string.heart_icon);
        mFavoriteToggle.setTextColor(mPlace.isFavorited() ? heartRed : darkGray);

        mPhotosAdapter = new PlacePhotosAdapter(this, this);
        mPhotos.setAdapter(mPhotosAdapter);
        mReviewsAdapter = new PlaceReviewsAdapter(this);

        mRestClient = RestClient.getInstance();
        mRestClient.registerPhotosListener(this);
        mRestClient.fetchPlacePhotos(mPlace);
        mRestClient.registerReviewsListener(this);
        mRestClient.fetchPlaceReviews(mPlace);

        mPlaceInfoView = new PlaceInfoView(
                this,
                mPlaceInfo,
                new IconDrawable(this, IoniconsIcons.ion_location).colorRes(R.color.dark_gray));
        boolean fromFavorites = getIntent().getBooleanExtra(FROM_FAVORITES, false);
        mPlaceInfoView.loadPlace(mPlace, fromFavorites);
    }

    @OnClick(R.id.favorite_toggle)
    public void onFavoriteClick() {
        mPlace.toggleFavorite();
        UIUtils.animateFavoriteToggle(mFavoriteToggle, mPlace.isFavorited());
        if (mPlace.isFavorited()) {
            DatabaseManager.get().getPlacesDBManager().addFavorite(mPlace);
        } else {
            DatabaseManager.get().getPlacesDBManager().removeFavorite(mPlace);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.setOnMapClickListener(mMapClickListener);

        LatLng place = new LatLng(mPlace.getLatitude(), mPlace.getLongitude());
        googleMap.addMarker(new MarkerOptions()
                .position(place)
                .title(mPlace.getName()));
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
            String mapUri = "google.navigation:q=" + mPlace.getAddress() + " " + mPlace.getName();
            startActivity(Intent.createChooser(
                    new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(mapUri)),
                    getString(R.string.navigate_with)));
        }
    };

    @OnClick(R.id.place_thumbnail)
    public void onThumbnailClicked() {
        if (TextUtils.isEmpty(mPlace.getImageUrl())) {
            return;
        }

        Intent intent = new Intent(this, PictureFullViewActivity.class);
        ArrayList<String> imageUrl = new ArrayList<>();
        imageUrl.add(mPlace.getImageUrl());
        intent.putStringArrayListExtra(PictureFullViewActivity.IMAGE_URLS_KEY, imageUrl);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @OnClick(R.id.call_button)
    public void callPlace() {
        String phoneUri = "tel:" + mPlace.getPhoneNumber();
        startActivity(Intent.createChooser(
                new Intent(Intent.ACTION_DIAL, Uri.parse(phoneUri)),
                getString(R.string.call_with)));
    }

    @OnClick(R.id.share_button)
    public void sharePlace() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mPlace.getUrl())
                .getIntent();
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(shareIntent);
        }
    }

    @Override
    public void onPhotosFetched(List<String> photos) {
        mPhotosStub.setVisibility(View.INVISIBLE);
        mPhotosAdapter.setPhotoUrls(photos);
        mPhotos.setVisibility(View.VISIBLE);
    }

    @Override
    public void onReviewsFetched(List<PlaceReview> reviews) {
        mReviewsStub.setVisibility(View.GONE);
        mReviewsAdapter.setReviews(reviews, mReviewsContainer, this);
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
        mPlaceMap.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPlaceMap.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPlaceMap.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPlaceMap.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPlaceMap.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mPlaceMap.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlaceMap.onDestroy();

        // Stop listening for photo fetch results
        mRestClient.cancelPhotosFetch();
        mRestClient.unregisterPhotosListener();

        // Stop listening for review fetch results
        mRestClient.cancelReviewsFetch();
        mRestClient.unregisterReviewsListener();
    }
}
