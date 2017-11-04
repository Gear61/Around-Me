package com.randomappsinc.aroundme.fragments;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.randomappsinc.aroundme.R;

public class HomepageFragmentController {

    private FragmentManager mFragmentManager;
    private int mContainerId;
    private PlaceTypesFragment mPlaceTypesFragment;
    private EventSearchFragment mEventSearchFragment;
    private FavoritesFragment mFavoritesFragment;
    private SettingsFragment mSettingsFragment;

    @IdRes private int mCurrentViewId;

    public HomepageFragmentController(FragmentManager fragmentManager, int containerId) {
        mFragmentManager = fragmentManager;
        mContainerId = containerId;
        mPlaceTypesFragment = PlaceTypesFragment.newInstance();
        mEventSearchFragment = EventSearchFragment.newInstance();
        mFavoritesFragment = FavoritesFragment.newInstance();
        mSettingsFragment = SettingsFragment.newInstance();
    }

    public void onNavItemSelected(@IdRes int viewId) {
        if (mCurrentViewId == viewId) {
            return;
        }

        mCurrentViewId = viewId;
        switch (viewId) {
            case R.id.home:
                swapInFragment(mPlaceTypesFragment);
                break;
            case R.id.events:
                swapInFragment(mEventSearchFragment);
                break;
            case R.id.favorites:
                swapInFragment(mFavoritesFragment);
                break;
            case R.id.settings:
                swapInFragment(mSettingsFragment);
                break;
        }
    }

    /** Called by the app upon start up to load the home fragment */
    public void loadHome() {
        mCurrentViewId = R.id.home;
        swapInFragment(mPlaceTypesFragment);
    }

    private void swapInFragment(Fragment fragment) {
        mFragmentManager.beginTransaction().replace(mContainerId, fragment).commit();
    }

    public void alertEventsOfLocationServicesGrant() {
        if (mEventSearchFragment != null) {
            mEventSearchFragment.onLocationServicesGranted();
        }
    }

    public void alertEventsOfLocationServicesDenial() {
        if (mEventSearchFragment != null) {
            mEventSearchFragment.onLocationServicesDenied();
        }
    }
}
