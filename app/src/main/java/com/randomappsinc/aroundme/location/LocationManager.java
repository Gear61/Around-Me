package com.randomappsinc.aroundme.location;

import android.Manifest;
import android.app.Activity;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.utils.PermissionUtils;
import com.randomappsinc.aroundme.utils.UIUtils;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class LocationManager implements LocationForm.Listener{

    // NOTE: If an activity uses this class, IT CANNOT USE MATCHING CODES
    public static final int LOCATION_SERVICES_CODE = 350;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 9001;

    public interface Listener {
        void onLocationFetched(String location);

        void onServicesOrPermissionChoice();
    }

    @NonNull private Listener listener;
    @NonNull private Activity activity;
    private Fragment fragment;

    private boolean locationFetched;
    private Handler locationChecker;
    private Runnable locationCheckTask;
    private LocationServicesManager locationServicesManager;
    private MaterialDialog locationDenialDialog;
    private MaterialDialog locationPermissionDialog;
    private LocationForm locationForm;

    public LocationManager(@NonNull Listener listener, @NonNull Activity activity) {
        this.listener = listener;
        this.activity = activity;
        initNonContext();
    }

    public LocationManager(@NonNull Listener listener, Fragment fragment) {
        this.listener = listener;
        activity = fragment.getActivity();
        this.fragment = fragment;
        initNonContext();
    }

    private void initNonContext() {
        locationServicesManager = new LocationServicesManager(activity);
        locationChecker = new Handler();
        locationCheckTask = new Runnable() {
            @Override
            public void run() {
                SmartLocation.with(activity).location().stop();
                if (!locationFetched) {
                    UIUtils.showToast(R.string.auto_location_fail);
                }
            }
        };

        locationForm = new LocationForm(activity, this);
        locationDenialDialog = new MaterialDialog.Builder(activity)
                .cancelable(false)
                .title(R.string.location_services_needed)
                .content(R.string.location_services_denial)
                .positiveText(R.string.location_services_confirm)
                .negativeText(R.string.enter_location_manually)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        locationServicesManager.askForLocationServices(LOCATION_SERVICES_CODE);
                        listener.onServicesOrPermissionChoice();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        locationForm.show();
                        listener.onServicesOrPermissionChoice();
                    }
                })
                .build();

        locationPermissionDialog = new MaterialDialog.Builder(activity)
                .cancelable(false)
                .title(R.string.location_permission_needed)
                .content(R.string.location_permission_denial)
                .positiveText(R.string.give_location_permission)
                .negativeText(R.string.enter_location_manually)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        requestLocationPermission();
                        listener.onServicesOrPermissionChoice();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        locationForm.show();
                        listener.onServicesOrPermissionChoice();
                    }
                })
                .build();
    }

    @Override
    public void onLocationEntered(String location) {
        stopFetchingCurrentLocation();
        listener.onLocationFetched(location);
    }

    public void fetchCurrentLocation() {
        if (PermissionUtils.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (SmartLocation.with(activity).location().state().locationServicesEnabled()) {
                fetchAutomaticLocation();
            } else {
                locationServicesManager.askForLocationServices(LOCATION_SERVICES_CODE);
            }
        } else {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        if (fragment != null) {
            PermissionUtils.requestPermission(
                    fragment,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            PermissionUtils.requestPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public void fetchAutomaticLocation() {
        locationFetched = false;
        SmartLocation.with(activity).location()
                .oneFix()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        locationChecker.removeCallbacks(locationCheckTask);
                        locationFetched = true;
                        String currentLocation = String.valueOf(location.getLatitude())
                                + ", "
                                + String.valueOf(location.getLongitude());
                        listener.onLocationFetched(currentLocation);
                    }
                });
        locationChecker.postDelayed(locationCheckTask, 10000L);
    }

    public void stopFetchingCurrentLocation() {
        locationChecker.removeCallbacks(locationCheckTask);
        SmartLocation.with(activity).location().stop();
    }

    public void showLocationForm() {
        locationForm.show();
    }

    public void showLocationDenialDialog() {
        locationDenialDialog.show();
    }

    public void showLocationPermissionDialog() {
        locationPermissionDialog.show();
    }
}
