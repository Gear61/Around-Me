package com.randomappsinc.aroundme.fragments;

import android.app.Fragment;

public class EventSearchFragment extends Fragment {

    public static EventSearchFragment newInstance() {
        EventSearchFragment fragment = new EventSearchFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }
}
