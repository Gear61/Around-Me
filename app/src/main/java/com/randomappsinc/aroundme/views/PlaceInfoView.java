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

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceInfoView {

    @BindView(R.id.place_thumbnail) ImageView thumbnail;
    @BindView(R.id.place_name) TextView name;
    @BindView(R.id.rating) ImageView rating;
    @BindView(R.id.num_reviews) TextView numReviews;
    @BindView(R.id.place_address) TextView address;
    @BindView(R.id.distance) TextView distance;
    @BindView(R.id.is_closed) TextView isClosed;

    @BindColor(R.color.green) int green;
    @BindColor(R.color.red) int red;
    @BindDrawable(R.drawable.gray_border) Drawable grayBorder;

    private Context mContext;
    private Drawable mDefaultThumbnail;

    public PlaceInfoView(Context context, View view, Drawable defaultThumbnail) {
        mContext = context;
        mDefaultThumbnail = defaultThumbnail;
        ButterKnife.bind(this, view);
    }

    public void loadPlace(Place place) {
        String placeImageUrl = place.getImageUrl();
        if (placeImageUrl != null && !placeImageUrl.isEmpty()) {
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
                .load(UIUtils.getRatingDrawableId(place.getRating()))
                .into(rating);

        String numReviewsText = place.getReviewCount() == 1
                ? mContext.getString(R.string.one_review)
                : String.format(mContext.getString(R.string.num_reviews), place.getReviewCount());
        numReviews.setText(numReviewsText);

        String distanceText = String.format(mContext.getString(R.string.miles_away), place.getDistance());
        distance.setText(distanceText);

        isClosed.setTextColor(place.isClosed() ? red : green);
        isClosed.setText(place.isClosed() ? R.string.closed : R.string.open);
    }
}
