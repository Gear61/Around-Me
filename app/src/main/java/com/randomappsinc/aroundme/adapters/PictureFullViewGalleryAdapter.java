package com.randomappsinc.aroundme.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.randomappsinc.aroundme.fragments.PictureFullViewFragment;

import java.util.List;

public class PictureFullViewGalleryAdapter extends FragmentStatePagerAdapter {

    private List<String> mImageUrls;

    public PictureFullViewGalleryAdapter(FragmentManager fragmentManager, List<String> imageUrls) {
        super(fragmentManager);
        mImageUrls = imageUrls;
    }

    @Override
    public Fragment getItem(int position) {
        return PictureFullViewFragment.newInstance(mImageUrls.get(position));
    }

    @Override
    public int getCount() {
        return mImageUrls.size();
    }
}
