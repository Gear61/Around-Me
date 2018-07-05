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

    @NonNull private Listener listener;
    private ArrayList<String> photoUrls;
    private Drawable defaultThumbnail;

    public PlacePhotosAdapter(Context context, @NonNull Listener listener) {
        this.listener = listener;
        photoUrls = new ArrayList<>();
        defaultThumbnail = new IconDrawable(context, IoniconsIcons.ion_image).colorRes(R.color.dark_gray);
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls.clear();
        this.photoUrls.addAll(photoUrls);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlacePhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_photo_cell, parent, false);
        return new PlacePhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacePhotoViewHolder holder, int position) {
        holder.loadPhoto(position);
    }

    @Override
    public int getItemCount() {
        return photoUrls.isEmpty() ? 1 : photoUrls.size();
    }

    class PlacePhotoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.place_photo) ImageView mPhotoView;
        @BindView(R.id.no_photos) View mNoPhotos;

        PlacePhotoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void loadPhoto(int position) {
            if (photoUrls.isEmpty()) {
                mPhotoView.setVisibility(View.GONE);
                mNoPhotos.setVisibility(View.VISIBLE);
            } else {
                mNoPhotos.setVisibility(View.GONE);
                mPhotoView.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(photoUrls.get(position))
                        .error(defaultThumbnail)
                        .fit()
                        .centerCrop()
                        .into(mPhotoView);
            }
        }

        @OnClick(R.id.parent)
        public void onPhotoClicked() {
            if (photoUrls.isEmpty()) {
                return;
            }

            listener.onPhotoClicked(photoUrls, getAdapterPosition());
        }
    }
}
