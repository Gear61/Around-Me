package com.randomappsinc.aroundme.adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.aroundme.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlacePhotosAdapter extends RecyclerView.Adapter<PlacePhotosAdapter.PlacePhotoViewHolder> {

    private Activity mActivity;
    private List<String> mPhotoUrls;
    private Drawable mDefaultThumbnail;

    public PlacePhotosAdapter(Activity activity) {
        mActivity = activity;
        mPhotoUrls = new ArrayList<>();
        mDefaultThumbnail = new IconDrawable(activity, IoniconsIcons.ion_image).colorRes(R.color.dark_gray);
    }

    public void setPhotoUrls(List<String> photoUrls) {
        mPhotoUrls.clear();
        mPhotoUrls.addAll(photoUrls);
        notifyDataSetChanged();
    }

    @Override
    public PlacePhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mActivity).inflate(R.layout.place_photo_cell, parent, false);
        return new PlacePhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlacePhotoViewHolder holder, int position) {
        holder.loadPhoto(position);
    }

    @Override
    public int getItemCount() {
        return mPhotoUrls.size();
    }

    class PlacePhotoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.place_photo) ImageView mPhotoView;

        PlacePhotoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void loadPhoto(int position) {
            Picasso.with(mActivity)
                    .load(mPhotoUrls.get(position))
                    .error(mDefaultThumbnail)
                    .fit()
                    .centerCrop()
                    .into(mPhotoView);
        }
    }
}
