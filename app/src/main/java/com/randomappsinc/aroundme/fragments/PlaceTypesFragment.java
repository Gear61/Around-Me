package com.randomappsinc.aroundme.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.activities.FilterActivity;
import com.randomappsinc.aroundme.activities.PlaceSearchActivity;
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

    public static PlaceTypesFragment newInstance() {
        PlaceTypesFragment fragment = new PlaceTypesFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.parent) View mParent;
    @BindView(R.id.place_types) RecyclerView mPlaceTypes;
    @BindView(R.id.add_place_type) FloatingActionButton mAddPlaceType;

    private PlaceTypeAdder mPlaceTypeAdder;
    private PlaceTypesAdapter mPlaceTypesAdapter;
    private Unbinder mUnbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.place_types, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        mToolbar.setTitle(R.string.app_name);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);

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
        Intent intent = new Intent(getActivity(), PlaceSearchActivity.class);
        intent.putExtra(PlaceSearchActivity.SEARCH_TERM_KEY, placeType.getText());
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_with_filter, menu);
        UIUtils.loadMenuIcon(menu, R.id.filter, IoniconsIcons.ion_funnel, getActivity());
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter:
                startActivity(new Intent(getActivity(), FilterActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
