package com.randomappsinc.aroundme.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.adapters.PlacesAdapter;
import com.randomappsinc.aroundme.api.RestClient;
import com.randomappsinc.aroundme.location.LocationManager;
import com.randomappsinc.aroundme.models.Place;
import com.randomappsinc.aroundme.utils.SimpleDividerItemDecoration;
import com.randomappsinc.aroundme.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceSearchActivity extends StandardActivity
        implements RestClient.PlacesListener, PlacesAdapter.ItemSelectionListener, LocationManager.Listener {

    public static final String SEARCH_TERM_KEY = "searchTerm";

    @BindView(R.id.parent) View mParent;
    @BindView(R.id.skeleton_results) View mSkeletonResults;
    @BindView(R.id.search_results) RecyclerView mPlaces;
    @BindView(R.id.no_places) View mNoPlaces;

    private String mSearchTerm;
    @Nullable private String mCurrentLocation;
    private RestClient mRestClient;
    private PlacesAdapter mPlacesAdapter;
    private LocationManager mLocationManager;
    private boolean mDenialLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_search);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mSearchTerm = getIntent().getStringExtra(SEARCH_TERM_KEY);
        setTitle(mSearchTerm);

        mRestClient = RestClient.getInstance();
        mRestClient.registerPlacesListener(this);

        mLocationManager = new LocationManager(this, this);

        mPlaces.addItemDecoration(new SimpleDividerItemDecoration(this));
        mPlacesAdapter = new PlacesAdapter(this, this);
        mPlaces.setAdapter(mPlacesAdapter);
    }

    @Override
    public void onPlacesFetched(List<Place> places) {
        mSkeletonResults.setVisibility(View.GONE);

        if (places.isEmpty()) {
            mPlaces.setVisibility(View.GONE);
            mNoPlaces.setVisibility(View.VISIBLE);
        } else {
            mNoPlaces.setVisibility(View.GONE);
            mPlacesAdapter.setPlaces(places);
            mPlaces.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPlaceClicked(Place place) {
        Intent intent = new Intent(this, PlaceViewActivity.class);
        intent.putExtra(PlaceViewActivity.PLACE_KEY, place);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Run this here instead of onCreate() to cover the case where they return from turning on location
        if (mCurrentLocation == null && !mDenialLock) {
            mLocationManager.fetchCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode != LocationManager.LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        // No need to check if the location permission has been granted because of the onResume() block
        if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            mDenialLock = true;
            mLocationManager.showLocationPermissionDialog();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != LocationManager.LOCATION_SERVICES_CODE) {
            return;
        }
        if (resultCode == RESULT_OK) {
            UIUtils.showSnackbar(mParent, R.string.location_services_on);
            mLocationManager.fetchAutomaticLocation();
        } else {
            mDenialLock = true;
            mLocationManager.showLocationDenialDialog();
        }
    }

    @Override
    public void onLocationFetched(String location) {
        if (mCurrentLocation != null && mCurrentLocation.equals(location)) {
            return;
        }

        mCurrentLocation = location;
        mRestClient.findPlaces(mSearchTerm, mCurrentLocation);
    }

    @Override
    public void onServicesOrPermissionChoice() {
        mDenialLock = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mLocationManager.stopFetchingCurrentLocation();

        // Stop listening for place search results
        mRestClient.cancelPlacesFetch();
        mRestClient.unregisterPlacesListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        UIUtils.loadMenuIcon(menu, R.id.set_location, IoniconsIcons.ion_android_map, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_location:
                mLocationManager.showLocationForm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
