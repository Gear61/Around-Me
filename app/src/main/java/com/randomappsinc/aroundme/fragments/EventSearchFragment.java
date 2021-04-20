package com.randomappsinc.aroundme.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.activities.EventViewActivity;
import com.randomappsinc.aroundme.adapters.EventsAdapter;
import com.randomappsinc.aroundme.api.RestClient;
import com.randomappsinc.aroundme.location.LocationManager;
import com.randomappsinc.aroundme.models.Event;
import com.randomappsinc.aroundme.utils.SimpleDividerItemDecoration;
import com.randomappsinc.aroundme.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EventSearchFragment extends Fragment
        implements RestClient.EventsListener, LocationManager.Listener, EventsAdapter.ItemSelectionListener {

    public static EventSearchFragment newInstance() {
        EventSearchFragment fragment = new EventSearchFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.parent) View parent;
    @BindView(R.id.skeleton_results) View skeletonResults;
    @BindView(R.id.events_list) RecyclerView eventsList;
    @BindView(R.id.no_events) View noEvents;

    private List<Event> events;

    private Unbinder unbinder;
    @Nullable private String currentLocation;
    private RestClient restClient = RestClient.getInstance();
    private EventsAdapter eventsAdapter;
    private LocationManager locationManager;
    private boolean denialLock;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_search, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        toolbar.setTitle(R.string.events);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        eventsList.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        eventsAdapter = new EventsAdapter(getActivity(), this);
        eventsList.setAdapter(eventsAdapter);

        restClient.registerEventsListener(this);

        locationManager = new LocationManager(this, this);

        if (events != null) {
            onEventsFetched(events);
        }

        return rootView;
    }

    @Override
    public void onEventsFetched(List<Event> events) {
        this.events = events;
        skeletonResults.setVisibility(View.GONE);

        if (events.isEmpty()) {
            eventsList.setVisibility(View.GONE);
            noEvents.setVisibility(View.VISIBLE);
        } else {
            noEvents.setVisibility(View.GONE);
            eventsAdapter.setEvents(events);
            eventsList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEventClicked(Event event) {
        Intent intent = new Intent(getActivity(), EventViewActivity.class);
        intent.putExtra(EventViewActivity.EVENT_KEY, event);
        getActivity().startActivity(intent);
    }

    @Override
    public void onResume() {
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

    public void onLocationServicesGranted() {
        UIUtils.showSnackbar(parent, R.string.location_services_on);
        locationManager.fetchAutomaticLocation();
    }

    public void onLocationServicesDenied() {
        denialLock = true;
        locationManager.showLocationDenialDialog();
    }

    @Override
    public void onLocationFetched(String location) {
        // No need to do a search on the same location
        if (currentLocation != null && currentLocation.equals(location)) {
            return;
        }

        currentLocation = location;
        restClient.findEvents(currentLocation);
    }

    @Override
    public void onServicesOrPermissionChoice() {
        denialLock = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

        locationManager.stopFetchingCurrentLocation();

        // Stop listening for event search results
        restClient.cancelEventsFetch();
        restClient.unregisterEventsListener();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_with_set_location, menu);
        UIUtils.loadMenuIcon(menu, R.id.set_location, IoniconsIcons.ion_android_map, getActivity());
        super.onCreateOptionsMenu(menu, inflater);
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
