package com.randomappsinc.aroundme.location;

import android.Manifest;
import android.app.Activity;
import android.location.Location;
import android.os.Handler;
import androidx.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.utils.PermissionUtils;
import com.randomappsinc.aroundme.utils.UIUtils;

public class LocationManager implements LocationForm.Listener {

    // NOTE: If an activity uses this class, IT CANNOT USE MATCHING CODES
    public static final int LOCATION_SERVICES_CODE = 350;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 9001;

    private static final long DESIRED_LOCATION_TURNAROUND = 1000L;

    public interface Listener {
        void onLocationFetched(String location);

        void onServicesOrPermissionChoice();
    }

    private Handler locationChecker = new Handler();
    private final Runnable locationCheckTask = new Runnable() {
        @Override
        public void run() {
            stopFetchingCurrentLocation();
            if (!locationFetched) {
                onLocationFetchFail();
            }
        }
    };

    @NonNull private Listener listener;
    @NonNull private Activity activity;
    private Fragment fragment;

    private FusedLocationProviderClient locationFetcher;
    private LocationRequest locationRequest;

    private boolean locationFetched;
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
        locationFetcher = LocationServices.getFusedLocationProviderClient(activity);
        locationRequest = LocationRequest.create()
                .setInterval(DESIRED_LOCATION_TURNAROUND)
                .setFastestInterval(DESIRED_LOCATION_TURNAROUND)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationServicesManager = new LocationServicesManager(activity);
        locationChecker = new Handler();

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
            checkLocationServicesAndFetchLocationIfOn();
        } else {
            requestLocationPermission();
        }
    }

    private void checkLocationServicesAndFetchLocationIfOn() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(activity);
        client.checkLocationSettings(builder.build())
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        fetchAutomaticLocation();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        if (exception instanceof ResolvableApiException) {
                            locationServicesManager.askForLocationServices(LOCATION_SERVICES_CODE);
                        } else {
                            onLocationFetchFail();
                        }
                    }
                });
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
        try {
            locationFetcher.requestLocationUpdates(locationRequest, locationCallback, null);
        } catch (SecurityException exception) {
            requestLocationPermission();
        }
    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null) {
                stopFetchingCurrentLocation();
                locationFetched = true;
                Location location = locationResult.getLastLocation();
                String latLongString = String.valueOf(location.getLatitude())
                        + ", "
                        + String.valueOf(location.getLongitude());
                listener.onLocationFetched(latLongString);
            }
        }
    };

    public void stopFetchingCurrentLocation() {
        locationChecker.removeCallbacks(locationCheckTask);
        locationFetcher.removeLocationUpdates(locationCallback);
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

    private void onLocationFetchFail() {
        UIUtils.showLongToast(R.string.auto_location_fail);
    }
}
