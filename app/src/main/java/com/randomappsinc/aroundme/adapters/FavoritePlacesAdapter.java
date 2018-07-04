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
import com.randomappsinc.aroundme.persistence.DatabaseManager;
import com.randomappsinc.aroundme.views.PlaceInfoView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FavoritePlacesAdapter extends RecyclerView.Adapter<FavoritePlacesAdapter.PlaceViewHolder> {

    public interface ItemSelectionListener {
        void onPlaceClicked(Place place);
    }

    @NonNull private ItemSelectionListener mItemSelectionListener;
    private Context mContext;
    @NonNull private List<Place> places;
    private Drawable defaultThumbnail;

    public FavoritePlacesAdapter(Context context, @NonNull ItemSelectionListener itemSelectionListener) {
        mItemSelectionListener = itemSelectionListener;
        mContext = context;
        places = DatabaseManager.get().getPlacesDBManager().getFavoritePlaces();
        defaultThumbnail = new IconDrawable(mContext, IoniconsIcons.ion_location).colorRes(R.color.dark_gray);
    }

    public void resyncWithDB() {
        places.clear();
        places.addAll(DatabaseManager.get().getPlacesDBManager().getFavoritePlaces());
        notifyDataSetChanged();
    }

    private Place getItem(int position) {
        return places.get(position);
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_cell, parent, false);
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
            mPlaceInfoView = new PlaceInfoView(mContext, placeInfo, defaultThumbnail);
        }

        void loadPlace(int position) {
            mPlaceInfoView.loadPlace(getItem(position), true);
        }

        @OnClick(R.id.place_info_parent)
        void onPlaceClicked() {
            Place place = getItem(getAdapterPosition());
            mItemSelectionListener.onPlaceClicked(place);
        }
    }
}
