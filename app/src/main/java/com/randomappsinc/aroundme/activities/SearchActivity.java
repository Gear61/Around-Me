package com.randomappsinc.aroundme.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.dialogs.LocationForm;
import com.randomappsinc.aroundme.utils.LocationServicesManager;
import com.randomappsinc.aroundme.utils.PermissionUtils;
import com.randomappsinc.aroundme.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class SearchActivity extends StandardActivity implements LocationForm.Listener {

    public static final String SEARCH_TERM_KEY = "searchTerm";
    private static final int LOCATION_SERVICES_CODE = 1;

    @BindView(R.id.parent) View mParent;

    private String mSearchTerm;
    @Nullable private String mCurrentLocation;

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
        setContentView(R.layout.search);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mSearchTerm = getIntent().getStringExtra(SEARCH_TERM_KEY);
        setTitle(mSearchTerm);

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
                        // TODO: Make search API call here
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
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchCurrentLocation();
        } else {
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
        // TODO: Make search API call here
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
