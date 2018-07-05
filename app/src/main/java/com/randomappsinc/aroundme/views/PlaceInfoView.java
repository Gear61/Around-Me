package com.randomappsinc.aroundme.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.constants.DistanceUnit;
import com.randomappsinc.aroundme.models.Place;
import com.randomappsinc.aroundme.persistence.PreferencesManager;
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

    private Context context;
    private Drawable defaultThumbnail;

    public PlaceInfoView(View view, Drawable defaultThumbnail) {
        this.context = view.getContext();
        this.defaultThumbnail = defaultThumbnail;
        ButterKnife.bind(this, view);
    }

    public void loadPlace(Place place, boolean fromFavorites) {
        String placeImageUrl = place.getImageUrl();
        if (placeImageUrl != null && !placeImageUrl.isEmpty()) {
            thumbnail.setBackground(null);
            Picasso.get()
                    .load(placeImageUrl)
                    .error(defaultThumbnail)
                    .fit().centerCrop()
                    .into(thumbnail);
        } else {
            thumbnail.setBackground(grayBorder);
            thumbnail.setImageDrawable(defaultThumbnail);
        }
        name.setText(place.getName());
        Picasso.get()
                .load(UIUtils.getRatingDrawableId(place.getRating()))
                .into(rating);

        String numReviewsText = place.getReviewCount() == 1
                ? context.getString(R.string.one_review)
                : String.format(context.getString(R.string.num_reviews), place.getReviewCount());
        numReviews.setText(numReviewsText);

        address.setText(place.getAddress());

        String categoriesText = place.getCategoriesListText();
        if (categoriesText.isEmpty()) {
            categories.setVisibility(View.GONE);
        } else {
            categories.setText(categoriesText);
            categories.setVisibility(View.VISIBLE);
        }

        if (fromFavorites) {
            distanceIcon.setVisibility(View.GONE);
            distance.setVisibility(View.GONE);
        } else {
            String distanceTemplate = PreferencesManager.get().getDistanceUnit().equals(DistanceUnit.MILES)
                    ? context.getString(R.string.miles_away)
                    : context.getString(R.string.kilometers_away);
            String distanceText = String.format(distanceTemplate, place.getDistance());
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
