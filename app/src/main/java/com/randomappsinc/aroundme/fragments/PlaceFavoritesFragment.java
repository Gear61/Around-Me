package com.randomappsinc.aroundme.fragments;

import android.app.Fragment;

public class PlaceFavoritesFragment extends Fragment {

    public static PlaceFavoritesFragment newInstance() {
        PlaceFavoritesFragment fragment = new PlaceFavoritesFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }
}
