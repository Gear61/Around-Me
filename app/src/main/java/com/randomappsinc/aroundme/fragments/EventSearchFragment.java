package com.randomappsinc.aroundme.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.randomappsinc.aroundme.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EventSearchFragment extends Fragment {

    private Unbinder mUnbinder;

    public static EventSearchFragment newInstance() {
        EventSearchFragment fragment = new EventSearchFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_search, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
