package com.randomappsinc.aroundme.fragments;

import android.app.Fragment;

public class FavoriteEventsFragment extends Fragment {

    public static FavoriteEventsFragment newInstance() {
        FavoriteEventsFragment fragment = new FavoriteEventsFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }
}
