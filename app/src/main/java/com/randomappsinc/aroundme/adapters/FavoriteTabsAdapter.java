package com.randomappsinc.aroundme.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.fragments.FavoriteEventsFragment;
import com.randomappsinc.aroundme.fragments.FavoritePlacesFragment;
import com.randomappsinc.aroundme.utils.MyApplication;

public class FavoriteTabsAdapter extends FragmentPagerAdapter {

    private String[] mFavoriteTabs;

    public FavoriteTabsAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        mFavoriteTabs = MyApplication.getAppContext().getResources().getStringArray(R.array.favorite_tabs);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FavoritePlacesFragment.newInstance();
            case 1:
                return FavoriteEventsFragment.newInstance();
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