package com.randomappsinc.aroundme.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.models.Place;
import com.randomappsinc.aroundme.views.PlaceInfoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {

    public interface ItemSelectionListener {
        void onPlaceClicked(Place place);
    }

    @NonNull private ItemSelectionListener mItemSelectionListener;
    private Context mContext;
    private List<Place> mPlaces = new ArrayList<>();
    private Drawable mDefaultThumbnail;

    public PlacesAdapter(Context context, @NonNull ItemSelectionListener itemSelectionListener) {
        mItemSelectionListener = itemSelectionListener;
        mContext = context;
        mDefaultThumbnail = new IconDrawable(mContext, IoniconsIcons.ion_location).colorRes(R.color.dark_gray);
    }

    public void setPlaces(List<Place> places) {
        mPlaces.clear();
        mPlaces.addAll(places);
        notifyDataSetChanged();
    }

    private Place getItem(int position) {
        return mPlaces.get(position);
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.place_cell, parent, false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        holder.loadPlace(position);
    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.parent) View placeInfo;

        @BindDrawable(R.drawable.gray_border) Drawable grayBorder;

        private PlaceInfoView mPlaceInfoView;

        PlaceViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mPlaceInfoView = new PlaceInfoView(mContext, placeInfo, mDefaultThumbnail);
        }

        void loadPlace(int position) {
            mPlaceInfoView.loadPlace(getItem(position));
        }

        @OnClick(R.id.parent)
        void onPlaceClicked() {
            Place place = getItem(getAdapterPosition());
            mItemSelectionListener.onPlaceClicked(place);
        }
    }
}