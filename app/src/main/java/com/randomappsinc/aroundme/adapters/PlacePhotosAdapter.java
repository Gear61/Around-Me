package com.randomappsinc.aroundme.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
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
import butterknife.OnClick;

public class PlacePhotosAdapter extends RecyclerView.Adapter<PlacePhotosAdapter.PlacePhotoViewHolder> {

    public interface Listener {
        void onPhotoClicked(ArrayList<String> imageUrls, int position);
    }

    @NonNull private Listener mListener;
    private Context mContext;
    private ArrayList<String> mPhotoUrls;
    private Drawable mDefaultThumbnail;

    public PlacePhotosAdapter(Context context, @NonNull Listener listener) {
        mListener = listener;
        mContext = context;
        mPhotoUrls = new ArrayList<>();
        mDefaultThumbnail = new IconDrawable(mContext, IoniconsIcons.ion_image).colorRes(R.color.dark_gray);
    }

    public void setPhotoUrls(List<String> photoUrls) {
        mPhotoUrls.clear();
        mPhotoUrls.addAll(photoUrls);
        notifyDataSetChanged();
    }

    @Override
    public PlacePhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.place_photo_cell, parent, false);
        return new PlacePhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlacePhotoViewHolder holder, int position) {
        holder.loadPhoto(position);
    }

    @Override
    public int getItemCount() {
        return mPhotoUrls.isEmpty() ? 1 : mPhotoUrls.size();
    }

    class PlacePhotoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.place_photo) ImageView mPhotoView;
        @BindView(R.id.no_photos) View mNoPhotos;

        PlacePhotoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void loadPhoto(int position) {
            if (mPhotoUrls.isEmpty()) {
                mPhotoView.setVisibility(View.GONE);
                mNoPhotos.setVisibility(View.VISIBLE);
            } else {
                mNoPhotos.setVisibility(View.GONE);
                mPhotoView.setVisibility(View.VISIBLE);
                Picasso.with(mContext)
                        .load(mPhotoUrls.get(position))
                        .error(mDefaultThumbnail)
                        .fit()
                        .centerCrop()
                        .into(mPhotoView);
            }
        }

        @OnClick(R.id.parent)
        public void onPhotoClicked() {
            if (mPhotoUrls.isEmpty()) {
                return;
            }

            mListener.onPhotoClicked(mPhotoUrls, getAdapterPosition());
        }
    }
}
