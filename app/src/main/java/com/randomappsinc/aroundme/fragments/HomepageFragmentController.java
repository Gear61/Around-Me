package com.randomappsinc.aroundme.fragments;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.randomappsinc.aroundme.R;

public class HomepageFragmentController {

    private FragmentManager fragmentManager;
    private int containerId;
    private PlaceTypesFragment placeTypesFragment;
    private EventSearchFragment eventSearchFragment;
    private FavoritesFragment favoritesFragment;
    private SettingsFragment settingsFragment;

    @IdRes private int currentViewId;

    public HomepageFragmentController(FragmentManager fragmentManager, int containerId) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        placeTypesFragment = PlaceTypesFragment.newInstance();
        eventSearchFragment = EventSearchFragment.newInstance();
        favoritesFragment = FavoritesFragment.newInstance();
        settingsFragment = SettingsFragment.newInstance();
    }

    public void onNavItemSelected(@IdRes int viewId) {
        if (currentViewId == viewId) {
            return;
        }

        currentViewId = viewId;
        switch (viewId) {
            case R.id.home:
                swapInFragment(placeTypesFragment);
                break;
            case R.id.events:
                swapInFragment(eventSearchFragment);
                break;
            case R.id.favorites:
                swapInFragment(favoritesFragment);
                break;
            case R.id.settings:
                swapInFragment(settingsFragment);
                break;
        }
    }

    /** Called by the app upon start up to load the home fragment */
    public void loadHome() {
        currentViewId = R.id.home;
        swapInFragment(placeTypesFragment);
    }

    private void swapInFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(containerId, fragment).commit();
    }

    public void alertEventsOfLocationServicesGrant() {
        if (eventSearchFragment != null) {
            eventSearchFragment.onLocationServicesGranted();
        }
    }

    public void alertEventsOfLocationServicesDenial() {
        if (eventSearchFragment != null) {
            eventSearchFragment.onLocationServicesDenied();
        }
    }
}
