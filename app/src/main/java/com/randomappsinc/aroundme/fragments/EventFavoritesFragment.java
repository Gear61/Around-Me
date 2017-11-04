package com.randomappsinc.aroundme.fragments;

import android.app.Fragment;

public class EventFavoritesFragment extends Fragment {

    public static EventFavoritesFragment newInstance() {
        EventFavoritesFragment fragment = new EventFavoritesFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }
}
