package com.randomappsinc.aroundme.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.randomappsinc.aroundme.R;

/** Utility class to ask for location services */
public class LocationServicesManager {

    private Activity mActivity;
    private LocationSettingsRequest.Builder mLocationBuilder;

    public LocationServicesManager(Activity activity) {
        mActivity = activity;
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationBuilder = new LocationSettingsRequest
                .Builder()
                .addLocationRequest(locationRequest);
        mLocationBuilder.setAlwaysShow(true);
    }

    public void askForLocationServices(final int requestCode) {
        Task<LocationSettingsResponse> result =
                LocationServices
                        .getSettingsClient(mActivity)
                        .checkLocationSettings(mLocationBuilder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    task.getResult(ApiException.class);
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show dialog to turn on location services
                                resolvable.startResolutionForResult(mActivity, requestCode);
                            } catch (IntentSender.SendIntentException |ClassCastException ignored) {}
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            openLocationSettings();
                            break;
                    }
                }
            }
        });
    }

    // Get location services the old fashioned way
    private void openLocationSettings() {
        Toast.makeText(mActivity, R.string.turn_on_location_services, Toast.LENGTH_LONG).show();
        mActivity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }
}
