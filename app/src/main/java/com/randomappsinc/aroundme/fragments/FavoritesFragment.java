package com.randomappsinc.aroundme.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
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
    @BindView(R.id.favorites_pager)
    ViewPager favoritesPager;

    private Unbinder unbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
