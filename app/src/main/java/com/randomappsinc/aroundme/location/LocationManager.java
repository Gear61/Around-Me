package com.randomappsinc.aroundme.location;

import android.Manifest;
import android.app.Activity;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;

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

    @NonNull private Listener mListener;
    private Activity mActivity;

    private boolean mLocationFetched;
    private Handler mLocationChecker;
    private Runnable mLocationCheckTask;
    private LocationServicesManager mLocationServicesManager;
    private MaterialDialog mLocationDenialDialog;
    private MaterialDialog mLocationPermissionDialog;
    private LocationForm mLocationForm;

    public LocationManager(@NonNull Listener listener, final Activity activity) {
        mListener = listener;
        mActivity = activity;

        mLocationServicesManager = new LocationServicesManager(mActivity);
        mLocationChecker = new Handler();
        mLocationCheckTask = new Runnable() {
            @Override
            public void run() {
                SmartLocation.with(activity).location().stop();
                if (!mLocationFetched) {
                    UIUtils.showToast(R.string.auto_location_fail);
                }
            }
        };

        mLocationForm = new LocationForm(mActivity, this);
        mLocationDenialDialog = new MaterialDialog.Builder(mActivity)
                .cancelable(false)
                .title(R.string.location_services_needed)
                .content(R.string.location_services_denial)
                .positiveText(R.string.location_services_confirm)
                .negativeText(R.string.enter_location_manually)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mLocationServicesManager.askForLocationServices(LOCATION_SERVICES_CODE);
                        mListener.onServicesOrPermissionChoice();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mLocationForm.show();
                        mListener.onServicesOrPermissionChoice();
                    }
                })
                .build();

        mLocationPermissionDialog = new MaterialDialog.Builder(mActivity)
                .cancelable(false)
                .title(R.string.location_permission_needed)
                .content(R.string.location_permission_denial)
                .positiveText(R.string.give_location_permission)
                .negativeText(R.string.enter_location_manually)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        requestLocationPermission();
                        mListener.onServicesOrPermissionChoice();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mLocationForm.show();
                        mListener.onServicesOrPermissionChoice();
                    }
                })
                .build();
    }

    @Override
    public void onLocationEntered(String location) {
        stopFetchingCurrentLocation();
        mListener.onLocationFetched(location);
    }

    public void fetchCurrentLocation() {
        if (PermissionUtils.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (SmartLocation.with(mActivity).location().state().locationServicesEnabled()) {
                fetchAutomaticLocation();
            } else {
                mLocationServicesManager.askForLocationServices(LOCATION_SERVICES_CODE);
            }
        } else {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        PermissionUtils.requestPermission(
                mActivity,
                Manifest.permission.ACCESS_FINE_LOCATION,
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    public void fetchAutomaticLocation() {
        mLocationFetched = false;
        SmartLocation.with(mActivity).location()
                .oneFix()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        mLocationChecker.removeCallbacks(mLocationCheckTask);
                        mLocationFetched = true;
                        String currentLocation = String.valueOf(location.getLatitude())
                                + ", "
                                + String.valueOf(location.getLongitude());
                        mListener.onLocationFetched(currentLocation);
                    }
                });
        mLocationChecker.postDelayed(mLocationCheckTask, 10000L);
    }

    public void stopFetchingCurrentLocation() {
        mLocationChecker.removeCallbacks(mLocationCheckTask);
        SmartLocation.with(mActivity).location().stop();
    }

    public void showLocationForm() {
        mLocationForm.show();
    }

    public void showLocationDenialDialog() {
        mLocationDenialDialog.show();
    }

    public void showLocationPermissionDialog() {
        mLocationPermissionDialog.show();
    }
}
