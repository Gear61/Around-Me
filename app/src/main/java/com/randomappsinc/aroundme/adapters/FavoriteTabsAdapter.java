package com.randomappsinc.aroundme.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.fragments.EventFavoritesFragment;
import com.randomappsinc.aroundme.fragments.PlaceFavoritesFragment;
import com.randomappsinc.aroundme.utils.MyApplication;

public class FavoriteTabsAdapter extends FragmentStatePagerAdapter {

    private String[] mFavoriteTabs;

    public FavoriteTabsAdapter (FragmentManager fragmentManager) {
        super(fragmentManager);
        mFavoriteTabs = MyApplication.getAppContext().getResources().getStringArray(R.array.favorite_tabs);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PlaceFavoritesFragment.newInstance();
            case 1:
                return EventFavoritesFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mFavoriteTabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFavoriteTabs[position];
    }
}