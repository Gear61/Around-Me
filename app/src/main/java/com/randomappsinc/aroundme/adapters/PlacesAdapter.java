package com.randomappsinc.aroundme.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.models.Place;
import com.randomappsinc.aroundme.utils.UIUtils;
import com.squareup.picasso.Picasso;

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
        @BindView(R.id.place_thumbnail) ImageView thumbnail;
        @BindView(R.id.place_name) TextView name;
        @BindView(R.id.rating) ImageView rating;
        @BindView(R.id.num_reviews) TextView numReviews;
        @BindView(R.id.place_address) TextView address;
        @BindView(R.id.distance) TextView distance;

        @BindDrawable(R.drawable.gray_border) Drawable grayBorder;

        PlaceViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void loadPlace(int position) {
            Place place = getItem(position);

            if (!place.getImageUrl().isEmpty()) {
                thumbnail.setBackground(null);
                Picasso.with(mContext)
                        .load(place.getImageUrl())
                        .error(mDefaultThumbnail)
                        .fit().centerCrop()
                        .into(thumbnail);
            } else {
                thumbnail.setBackground(grayBorder);
                thumbnail.setImageDrawable(mDefaultThumbnail);
            }
            name.setText(place.getName());
            address.setText(place.getAddress());
            Picasso.with(mContext)
                    .load(UIUtils.getRatingDrawableId(place))
                    .into(rating);

            String numReviewsText = place.getReviewCount() == 1
                    ? mContext.getString(R.string.one_review)
                    : String.format(mContext.getString(R.string.num_reviews), place.getReviewCount());
            numReviews.setText(numReviewsText);

            String distanceText = String.format(mContext.getString(R.string.miles_away), place.getDistance());
            distance.setText(distanceText);
        }

        @OnClick(R.id.parent)
        void onPlaceClicked() {
            Place place = getItem(getAdapterPosition());
            mItemSelectionListener.onPlaceClicked(place);
        }
    }
}
