package com.randomappsinc.aroundme.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.adapters.PlacePhotosAdapter;
import com.randomappsinc.aroundme.api.RestClient;
import com.randomappsinc.aroundme.api.models.PlaceReviews;
import com.randomappsinc.aroundme.models.Place;
import com.randomappsinc.aroundme.views.PlaceInfoView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlaceViewActivity extends StandardActivity
        implements RestClient.PhotosListener, RestClient.ReviewsListener {

    public static final String PLACE_KEY = "place";

    @BindView(R.id.parent) View mPlaceInfo;
    @BindView(R.id.place_photos) RecyclerView mPhotos;

    private Place mPlace;
    private PlaceInfoView mPlaceInfoView;
    private RestClient mRestClient;
    private PlacePhotosAdapter mPhotosAdapter;

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

        mPhotosAdapter = new PlacePhotosAdapter(this);
        mPhotos.setAdapter(mPhotosAdapter);

        mRestClient = RestClient.getInstance();
        mRestClient.registerPhotosListener(this);
        mRestClient.fetchPlacePhotos(mPlace);
        mRestClient.registerReviewsListener(this);
        mRestClient.fetchPlaceReviews(mPlace);

        mPlaceInfoView = new PlaceInfoView(
                this,
                mPlaceInfo,
                new IconDrawable(this, IoniconsIcons.ion_location).colorRes(R.color.dark_gray));
        mPlaceInfoView.loadPlace(mPlace);
    }

    @OnClick(R.id.navigate_button)
    public void navigateToPlace() {
        String mapUri = "google.navigation:q=" + mPlace.getAddress() + " " + mPlace.getName();
        startActivity(Intent.createChooser(
                new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(mapUri)),
                getString(R.string.navigate_with)));
    }

    @OnClick(R.id.call_button)
    public void callPlace() {
        String phoneUri = "tel:" + mPlace.getPhoneNumber();
        startActivity(Intent.createChooser(
                new Intent(Intent.ACTION_DIAL, Uri.parse(phoneUri)),
                getString(R.string.call_with)));
    }

    @Override
    public void onPhotosFetched(List<String> photos) {
        mPhotosAdapter.setPhotoUrls(photos);
    }

    @Override
    public void onReviewsFetched(List<PlaceReviews.Review> photos) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Stop listening for photo fetch results
        mRestClient.cancelPhotosFetch();
        mRestClient.unregisterPhotosListener();

        // Stop listening for review fetch results
        mRestClient.cancelReviewsFetch();
        mRestClient.unregisterReviewsListener();
    }
}
