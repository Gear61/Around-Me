package com.randomappsinc.aroundme.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.adapters.FavoriteTabsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavoritesFragment extends Fragment {

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.favorite_tabs_layout) TabLayout favoritesTypes;
    @BindView(R.id.favorites_pager) ViewPager favoritesPager;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorites, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        toolbar.setTitle(R.string.favorites);

        favoritesPager.setAdapter(new FavoriteTabsAdapter(getChildFragmentManager()));
        favoritesTypes.setupWithViewPager(favoritesPager);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
