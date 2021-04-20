package com.randomappsinc.aroundme.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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

    @BindView(R.id.favorite_places) RecyclerView places;
    @BindView(R.id.no_favorite_places) View noPlaces;

    private Unbinder unbinder;
    private FavoritePlacesAdapter placesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorite_places, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        places.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        placesAdapter = new FavoritePlacesAdapter(getActivity(), this);
        places.setAdapter(placesAdapter);

        refreshViews();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        placesAdapter.resyncWithDB();
        refreshViews();
    }

    private void refreshViews() {
        if (placesAdapter.getItemCount() == 0) {
            places.setVisibility(View.GONE);
            noPlaces.setVisibility(View.VISIBLE);
        } else {
            noPlaces.setVisibility(View.GONE);
            places.setVisibility(View.VISIBLE);
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
        unbinder.unbind();
    }
}
