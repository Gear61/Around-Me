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

    @BindView(R.id.favorite_events) RecyclerView events;
    @BindView(R.id.no_favorite_events) View noEvents;

    private Unbinder unbinder;
    private FavoriteEventsAdapter eventsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorite_events, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        events.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        eventsAdapter = new FavoriteEventsAdapter(getActivity(), this);
        events.setAdapter(eventsAdapter);

        refreshViews();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        eventsAdapter.resyncWithDB();
        refreshViews();
    }

    private void refreshViews() {
        if (eventsAdapter.getItemCount() == 0) {
            events.setVisibility(View.GONE);
            noEvents.setVisibility(View.VISIBLE);
        } else {
            noEvents.setVisibility(View.GONE);
            events.setVisibility(View.VISIBLE);
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
        unbinder.unbind();
    }
}
