package com.randomappsinc.aroundme.fragments;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.api.RestClient;
import com.randomappsinc.aroundme.location.LocationManager;
import com.randomappsinc.aroundme.models.Event;
import com.randomappsinc.aroundme.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EventSearchFragment extends Fragment
        implements RestClient.EventsListener, LocationManager.Listener {

    private Unbinder mUnbinder;

    public static EventSearchFragment newInstance() {
        EventSearchFragment fragment = new EventSearchFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @BindView(R.id.parent) View mParent;

    @Nullable private String mCurrentLocation;
    private RestClient mRestClient;
    private LocationManager mLocationManager;
    private boolean mDenialLock;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_search, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        mRestClient = RestClient.getInstance();
        mRestClient.registerEventsListener(this);

        mLocationManager = new LocationManager(this, this);

        return rootView;
    }

    @Override
    public void onEventsFetched(List<Event> events) {
        // TODO: Process events results
    }

    @Override
    public void onResume() {
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

    public void onLocationServicesGranted() {
        UIUtils.showSnackbar(mParent, R.string.location_services_on);
        mLocationManager.fetchAutomaticLocation();
    }

    public void onLocationServicesDenied() {
        mDenialLock = true;
        mLocationManager.showLocationDenialDialog();
    }

    @Override
    public void onLocationFetched(String location) {
        mCurrentLocation = location;
        mRestClient.findEvents(mCurrentLocation);
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
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
