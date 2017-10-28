package com.randomappsinc.aroundme.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.activities.SearchActivity;
import com.randomappsinc.aroundme.adapters.PlaceTypesAdapter;
import com.randomappsinc.aroundme.dialogs.PlaceTypeAdder;
import com.randomappsinc.aroundme.models.PlaceType;
import com.randomappsinc.aroundme.utils.SimpleDividerItemDecoration;
import com.randomappsinc.aroundme.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PlaceTypesFragment extends Fragment implements PlaceTypesAdapter.Listener {

    @BindView(R.id.parent) View mParent;
    @BindView(R.id.place_types) RecyclerView mPlaceTypes;
    @BindView(R.id.add_place_type) FloatingActionButton mAddPlaceType;

    private PlaceTypeAdder mPlaceTypeAdder;
    private PlaceTypesAdapter mPlaceTypesAdapter;
    private Unbinder mUnbinder;

    public static PlaceTypesFragment newInstance() {
        PlaceTypesFragment fragment = new PlaceTypesFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.place_types, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        mAddPlaceType.setImageDrawable(
                new IconDrawable(getActivity(), IoniconsIcons.ion_android_add).colorRes(R.color.white));

        mPlaceTypes.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mPlaceTypesAdapter = new PlaceTypesAdapter(getActivity(), this, mParent);
        mPlaceTypes.setAdapter(mPlaceTypesAdapter);

        mPlaceTypeAdder = new PlaceTypeAdder(getActivity(), mTypeAddedListener);

        return rootView;
    }

    @OnClick(R.id.add_place_type)
    public void addPlaceType() {
        mPlaceTypeAdder.show();
    }

    private final PlaceTypeAdder.Listener mTypeAddedListener = new PlaceTypeAdder.Listener() {
        @Override
        public void onPlaceTypeAdded() {
            mPlaceTypesAdapter.updateWithAdded();
            UIUtils.showSnackbar(mParent, R.string.place_type_added);
        }
    };

    @Override
    public void onItemClick(PlaceType placeType) {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra(SearchActivity.SEARCH_TERM_KEY, placeType.getText());
        getActivity().startActivity(intent);
    }

    @Override
    public void scrollToItem(int position) {
        mPlaceTypes.smoothScrollToPosition(position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
