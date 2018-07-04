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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {

    public interface ItemSelectionListener {
        void onPlaceClicked(Place place);
    }

    @NonNull private ItemSelectionListener itemSelectionListener;
    private Context context;
    private List<Place> places = new ArrayList<>();
    private Drawable defaultThumbnail;

    public PlacesAdapter(Context context, @NonNull ItemSelectionListener itemSelectionListener) {
        this.itemSelectionListener = itemSelectionListener;
        this.context = context;
        defaultThumbnail = new IconDrawable(this.context, IoniconsIcons.ion_location)
                .colorRes(R.color.dark_gray);
    }

    public void setPlaces(List<Place> places) {
        this.places.clear();
        this.places.addAll(places);
        notifyDataSetChanged();
    }

    private Place getItem(int position) {
        return places.get(position);
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.place_cell, parent, false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        holder.loadPlace(position);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.place_info_parent) View placeInfo;

        private PlaceInfoView mPlaceInfoView;

        PlaceViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mPlaceInfoView = new PlaceInfoView(context, placeInfo, defaultThumbnail);
        }

        void loadPlace(int position) {
            mPlaceInfoView.loadPlace(getItem(position), false);
        }

        @OnClick(R.id.place_info_parent)
        void onPlaceClicked() {
            Place place = getItem(getAdapterPosition());
            itemSelectionListener.onPlaceClicked(place);
        }
    }
}
