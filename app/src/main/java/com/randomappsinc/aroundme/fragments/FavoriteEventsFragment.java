package com.randomappsinc.aroundme.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.activities.EventViewActivity;
import com.randomappsinc.aroundme.adapters.FavoriteEventsAdapter;
import com.randomappsinc.aroundme.models.Event;
import com.randomappsinc.aroundme.utils.SimpleDividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavoriteEventsFragment extends Fragment implements FavoriteEventsAdapter.ItemSelectionListener {

    public static FavoriteEventsFragment newInstance() {
        return new FavoriteEventsFragment();
    }

    @BindView(R.id.favorite_events) RecyclerView mEvents;
    @BindView(R.id.no_favorite_events) View mNoEvents;

    private Unbinder mUnbinder;
    private FavoriteEventsAdapter mEventsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorite_events, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        mEvents.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mEventsAdapter = new FavoriteEventsAdapter(getActivity(), this);
        mEvents.setAdapter(mEventsAdapter);

        refreshViews();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mEventsAdapter.resyncWithDB();
        refreshViews();
    }

    private void refreshViews() {
        if (mEventsAdapter.getItemCount() == 0) {
            mEvents.setVisibility(View.GONE);
            mNoEvents.setVisibility(View.VISIBLE);
        } else {
            mNoEvents.setVisibility(View.GONE);
            mEvents.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEventClicked(Event event) {
        Intent intent = new Intent(getActivity(), EventViewActivity.class);
        intent.putExtra(EventViewActivity.EVENT_KEY, event);
        getActivity().startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
