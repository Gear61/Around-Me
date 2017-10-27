package com.randomappsinc.aroundme.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.adapters.PlacePhotosAdapter;
import com.randomappsinc.aroundme.adapters.PlaceReviewsAdapter;
import com.randomappsinc.aroundme.api.RestClient;
import com.randomappsinc.aroundme.models.Place;
import com.randomappsinc.aroundme.models.Review;
import com.randomappsinc.aroundme.views.PlaceInfoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlaceViewActivity extends StandardActivity implements RestClient.PhotosListener,
        RestClient.ReviewsListener, PlaceReviewsAdapter.Listener, PlacePhotosAdapter.Listener {

    public static final String PLACE_KEY = "place";

    @BindView(R.id.parent) View mPlaceInfo;
    @BindView(R.id.photos_stub) View mPhotosStub;
    @BindView(R.id.place_photos) RecyclerView mPhotos;
    @BindView(R.id.reviews_stub) View mReviewsStub;
    @BindView(R.id.place_reviews) RecyclerView mReviews;

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

        mPhotosAdapter = new PlacePhotosAdapter(this, this);
        mPhotos.setAdapter(mPhotosAdapter);
        mReviewsAdapter = new PlaceReviewsAdapter(this, this);
        mReviews.setAdapter(mReviewsAdapter);

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
        mPhotosStub.setVisibility(View.GONE);
        mPhotos.setVisibility(View.VISIBLE);
        mPhotosAdapter.setPhotoUrls(photos);
    }

    @Override
    public void onReviewsFetched(List<Review> reviews) {
        mReviewsStub.setVisibility(View.GONE);
        mReviews.setVisibility(View.VISIBLE);
        mReviewsAdapter.setReviews(reviews);
    }

    @Override
    public void onReviewClicked(Review review) {
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
