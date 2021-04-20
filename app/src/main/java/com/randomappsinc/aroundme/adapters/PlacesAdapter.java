package com.randomappsinc.aroundme.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    private List<Place> places = new ArrayList<>();
    private Drawable defaultThumbnail;

    public PlacesAdapter(Context context, @NonNull ItemSelectionListener itemSelectionListener) {
        this.itemSelectionListener = itemSelectionListener;
        defaultThumbnail = new IconDrawable(context, IoniconsIcons.ion_location)
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

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_cell, parent, false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
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
            mPlaceInfoView = new PlaceInfoView(placeInfo, defaultThumbnail);
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
