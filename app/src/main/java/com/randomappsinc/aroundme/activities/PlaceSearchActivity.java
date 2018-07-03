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

    private static final int FILTER_REQUEST_CODE = 1;

    @BindView(R.id.parent) View parent;
    @BindView(R.id.skeleton_results) View skeletonResults;
    @BindView(R.id.search_results) RecyclerView placesList;
    @BindView(R.id.no_places) View noPlaces;

    private String searchTerm;
    @Nullable private String currentLocation;
    private RestClient restClient;
    private PlacesAdapter placesAdapter;
    private LocationManager locationManager;
    private boolean denialLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_search);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        searchTerm = getIntent().getStringExtra(SEARCH_TERM_KEY);
        setTitle(searchTerm);

        restClient = RestClient.getInstance();
        restClient.registerPlacesListener(this);

        locationManager = new LocationManager(this, this);

        placesList.addItemDecoration(new SimpleDividerItemDecoration(this));
        placesAdapter = new PlacesAdapter(this, this);
        placesList.setAdapter(placesAdapter);
    }

    @Override
    public void onPlacesFetched(List<Place> places) {
        skeletonResults.setVisibility(View.GONE);

        if (places.isEmpty()) {
            placesList.setVisibility(View.GONE);
            noPlaces.setVisibility(View.VISIBLE);
        } else {
            noPlaces.setVisibility(View.GONE);
            placesAdapter.setPlaces(places);
            placesList.setVisibility(View.VISIBLE);
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
        if (currentLocation == null && !denialLock) {
            locationManager.fetchCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode != LocationManager.LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        // No need to check if the location permission has been granted because of the onResume() block
        if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            denialLock = true;
            locationManager.showLocationPermissionDialog();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LocationManager.LOCATION_SERVICES_CODE:
                if (resultCode == RESULT_OK) {
                    UIUtils.showSnackbar(parent, R.string.location_services_on);
                    locationManager.fetchAutomaticLocation();
                } else {
                    denialLock = true;
                    locationManager.showLocationDenialDialog();
                }
                break;
            case FILTER_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    placesList.setVisibility(View.GONE);
                    noPlaces.setVisibility(View.GONE);
                    skeletonResults.setVisibility(View.VISIBLE);
                    restClient.findPlaces(searchTerm, currentLocation);
                }
                break;
        }
    }

    @Override
    public void onLocationFetched(String location) {
        if (currentLocation != null && currentLocation.equals(location)) {
            return;
        }

        placesList.setVisibility(View.GONE);
        skeletonResults.setVisibility(View.VISIBLE);

        currentLocation = location;
        restClient.findPlaces(searchTerm, currentLocation);
    }

    @Override
    public void onServicesOrPermissionChoice() {
        denialLock = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        locationManager.stopFetchingCurrentLocation();

        // Stop listening for place search results
        restClient.cancelPlacesFetch();
        restClient.unregisterPlacesListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_place_search, menu);
        UIUtils.loadMenuIcon(menu, R.id.set_location, IoniconsIcons.ion_android_map, this);
        UIUtils.loadMenuIcon(menu, R.id.filter, IoniconsIcons.ion_funnel, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_location:
                locationManager.showLocationForm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
