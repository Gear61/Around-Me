package com.randomappsinc.aroundme.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.activities.PlaceViewActivity;
import com.randomappsinc.aroundme.adapters.FavoritePlacesAdapter;
import com.randomappsinc.aroundme.models.Place;
import com.randomappsinc.aroundme.utils.SimpleDividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavoritePlacesFragment extends Fragment implements FavoritePlacesAdapter.ItemSelectionListener {

    public static FavoritePlacesFragment newInstance() {
        return new FavoritePlacesFragment();
    }

    @BindView(R.id.favorite_places) RecyclerView mPlaces;
    @BindView(R.id.no_favorite_places) View mNoPlaces;

    private Unbinder mUnbinder;
    private FavoritePlacesAdapter mPlacesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorite_places, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        mPlaces.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mPlacesAdapter = new FavoritePlacesAdapter(getActivity(), this);
        mPlaces.setAdapter(mPlacesAdapter);

        refreshViews();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPlacesAdapter.resyncWithDB();
        refreshViews();
    }

    private void refreshViews() {
        if (mPlacesAdapter.getItemCount() == 0) {
            mPlaces.setVisibility(View.GONE);
            mNoPlaces.setVisibility(View.VISIBLE);
        } else {
            mNoPlaces.setVisibility(View.GONE);
            mPlaces.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onPlaceClicked(Place place) {
        Intent intent = new Intent(getActivity(), PlaceViewActivity.class);
        intent.putExtra(PlaceViewActivity.PLACE_KEY, place);
        intent.putExtra(PlaceViewActivity.FROM_FAVORITES, true);
        getActivity().startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
