package com.randomappsinc.aroundme.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.fragments.FavoriteEventsFragment;
import com.randomappsinc.aroundme.fragments.FavoritePlacesFragment;
import com.randomappsinc.aroundme.utils.MyApplication;

public class FavoriteTabsAdapter extends FragmentPagerAdapter {

    private String[] favoriteTabs;

    public FavoriteTabsAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        favoriteTabs = MyApplication.getAppContext().getResources().getStringArray(R.array.favorite_tabs);
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
        return favoriteTabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return favoriteTabs[position];
    }
}
