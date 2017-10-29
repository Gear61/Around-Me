package com.randomappsinc.aroundme.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.adapters.PlacesAdapter;
import com.randomappsinc.aroundme.api.RestClient;
import com.randomappsinc.aroundme.dialogs.LocationForm;
import com.randomappsinc.aroundme.models.Place;
import com.randomappsinc.aroundme.utils.LocationServicesManager;
import com.randomappsinc.aroundme.utils.PermissionUtils;
import com.randomappsinc.aroundme.utils.SimpleDividerItemDecoration;
import com.randomappsinc.aroundme.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class PlaceSearchActivity extends StandardActivity
        implements LocationForm.Listener, RestClient.PlacesListener, PlacesAdapter.ItemSelectionListener {

    public static final String SEARCH_TERM_KEY = "searchTerm";
    private static final int LOCATION_SERVICES_CODE = 1;

    @BindView(R.id.parent) View mParent;
    @BindView(R.id.skeleton_results) View mSkeletonResults;
    @BindView(R.id.search_results) RecyclerView mPlaces;
    @BindView(R.id.no_places) View mNoPlaces;

    private String mSearchTerm;
    @Nullable private String mCurrentLocation;
    private RestClient mRestClient;
    private PlacesAdapter mPlacesAdapter;

    // Location
    private boolean mLocationFetched;
    private Handler mLocationChecker;
    private Runnable mLocationCheckTask;
    private LocationServicesManager mLocationServicesManager;
    private boolean mDenialLock;
    private MaterialDialog mLocationDenialDialog;
    private MaterialDialog mLocationPermissionDialog;
    private LocationForm mLocationForm;

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

        mDenialLock = false;
        mLocationServicesManager = new LocationServicesManager(this);
        mLocationChecker = new Handler();
        mLocationCheckTask = new Runnable() {
            @Override
            public void run() {
                SmartLocation.with(getBaseContext()).location().stop();
                if (!mLocationFetched) {
                    UIUtils.showSnackbar(mParent, R.string.auto_location_fail);
                }
            }
        };

        mLocationForm = new LocationForm(this, this);
        mLocationDenialDialog = new MaterialDialog.Builder(this)
                .cancelable(false)
                .title(R.string.location_services_needed)
                .content(R.string.location_services_denial)
                .positiveText(R.string.location_services_confirm)
                .negativeText(R.string.enter_location_manually)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mLocationServicesManager.askForLocationServices(LOCATION_SERVICES_CODE);
                        mDenialLock = false;
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mLocationForm.show();
                        mDenialLock = false;
                    }
                })
                .build();

        mLocationPermissionDialog = new MaterialDialog.Builder(this)
                .cancelable(false)
                .title(R.string.location_permission_needed)
                .content(R.string.location_permission_denial)
                .positiveText(R.string.give_location_permission)
                .negativeText(R.string.enter_location_manually)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        requestLocationPermission();
                        mDenialLock = false;
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mLocationForm.show();
                        mDenialLock = false;
                    }
                })
                .build();

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
            fetchCurrentLocation();
        }
    }

    private void fetchCurrentLocation() {
        if (PermissionUtils.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (SmartLocation.with(this).location().state().locationServicesEnabled()) {
                runLocationFetch();
            } else {
                mLocationServicesManager.askForLocationServices(LOCATION_SERVICES_CODE);
            }
        } else {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        PermissionUtils.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, 1);
    }

    private void runLocationFetch() {
        mLocationFetched = false;
        SmartLocation.with(this).location()
                .oneFix()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        mLocationChecker.removeCallbacks(mLocationCheckTask);
                        mLocationFetched = true;
                        mCurrentLocation = String.valueOf(location.getLatitude())
                                + ", "
                                + String.valueOf(location.getLongitude());
                        mRestClient.findPlaces(mSearchTerm, mCurrentLocation);
                    }
                });
        mLocationChecker.postDelayed(mLocationCheckTask, 10000L);
    }

    private void stopFetchingCurrentLocation() {
        mLocationChecker.removeCallbacks(mLocationCheckTask);
        SmartLocation.with(this).location().stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        // No need to check if the location permission has been granted because of the onResume() block
        if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            mDenialLock = true;
            mLocationPermissionDialog.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != LOCATION_SERVICES_CODE) {
            return;
        }
        if (resultCode == RESULT_OK) {
            UIUtils.showSnackbar(mParent, R.string.location_services_on);
            runLocationFetch();
        } else {
            mDenialLock = true;
            mLocationDenialDialog.show();
        }
    }

    @Override
    public void onLocationEntered(String location) {
        // Cancel current location fetch when location is manually entered
        stopFetchingCurrentLocation();

        mCurrentLocation = location;
        mRestClient.findPlaces(mSearchTerm, mCurrentLocation);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopFetchingCurrentLocation();

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
                mLocationForm.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
