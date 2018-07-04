package com.randomappsinc.aroundme.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.randomappsinc.aroundme.fragments.PictureFullViewFragment;

import java.util.List;

public class PictureFullViewGalleryAdapter extends FragmentStatePagerAdapter {

    private List<String> imageUrls;

    public PictureFullViewGalleryAdapter(FragmentManager fragmentManager, List<String> imageUrls) {
        super(fragmentManager);
        this.imageUrls = imageUrls;
    }

    @Override
    public Fragment getItem(int position) {
        return PictureFullViewFragment.newInstance(imageUrls.get(position));
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }
}
