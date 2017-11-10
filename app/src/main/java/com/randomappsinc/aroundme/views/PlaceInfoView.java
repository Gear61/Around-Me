package com.randomappsinc.aroundme.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.models.Place;
import com.randomappsinc.aroundme.utils.UIUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceInfoView {

    @BindView(R.id.place_thumbnail) ImageView thumbnail;
    @BindView(R.id.place_name) TextView name;
    @BindView(R.id.rating) ImageView rating;
    @BindView(R.id.num_reviews) TextView numReviews;
    @BindView(R.id.place_address) TextView address;
    @BindView(R.id.categories) TextView categories;
    @BindView(R.id.distance_icon) View distanceIcon;
    @BindView(R.id.distance) TextView distance;
    @BindView(R.id.price) TextView price;

    @BindDrawable(R.drawable.gray_border) Drawable grayBorder;

    private Context mContext;
    private Drawable mDefaultThumbnail;

    public PlaceInfoView(Context context, View view, Drawable defaultThumbnail) {
        mContext = context;
        mDefaultThumbnail = defaultThumbnail;
        ButterKnife.bind(this, view);
    }

    public void loadPlace(Place place, boolean fromFavorites) {
        String placeImageUrl = place.getImageUrl();
        if (placeImageUrl != null && !placeImageUrl.isEmpty()) {
            thumbnail.setBackground(null);
            Picasso.with(mContext)
                    .load(placeImageUrl)
                    .error(mDefaultThumbnail)
                    .fit().centerCrop()
                    .into(thumbnail);
        } else {
            thumbnail.setBackground(grayBorder);
            thumbnail.setImageDrawable(mDefaultThumbnail);
        }
        name.setText(place.getName());
        Picasso.with(mContext)
                .load(UIUtils.getRatingDrawableId(place.getRating()))
                .into(rating);

        String numReviewsText = place.getReviewCount() == 1
                ? mContext.getString(R.string.one_review)
                : String.format(mContext.getString(R.string.num_reviews), place.getReviewCount());
        numReviews.setText(numReviewsText);

        address.setText(place.getAddress());
        categories.setText(place.getCategoriesListText());

        if (fromFavorites) {
            distanceIcon.setVisibility(View.GONE);
            distance.setVisibility(View.GONE);
        } else {
            String distanceText = String.format(mContext.getString(R.string.miles_away), place.getDistance());
            distance.setText(distanceText);
        }

        String placePrice = place.getPrice();
        if (placePrice == null || placePrice.isEmpty()) {
            price.setVisibility(View.GONE);
        } else {
            price.setText(place.getPrice());
            price.setVisibility(View.VISIBLE);
        }
    }
}
