package com.randomappsinc.aroundme.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.models.PlaceReview;
import com.randomappsinc.aroundme.utils.UIUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PlaceReviewCell {

    public interface Listener {
        void onReviewClicked(PlaceReview placeReview);
    }

    @BindView(R.id.user_image) CircleImageView userImage;
    @BindView(R.id.user_icon) View userIcon;
    @BindView(R.id.user_name) TextView userName;
    @BindView(R.id.review_text) TextView reviewText;
    @BindView(R.id.rating) ImageView rating;
    @BindView(R.id.review_date) TextView reviewDate;

    private Listener listener;
    private PlaceReview review;

    public PlaceReviewCell(View view, Listener listener) {
        ButterKnife.bind(this, view);
        this.listener = listener;
    }

    public void loadReview(PlaceReview review, Context context) {
        this.review = review;

        Drawable defaultThumbnail = new IconDrawable(context, IoniconsIcons.ion_android_person)
                .colorRes(R.color.dark_gray);

        String userImageUrl = review.getUserImageUrl();
        if (userImageUrl == null || userImageUrl.isEmpty()) {
            userImage.setVisibility(View.GONE);
            userIcon.setVisibility(View.VISIBLE);
        } else {
            userIcon.setVisibility(View.GONE);
            userImage.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(review.getUserImageUrl())
                    .error(defaultThumbnail)
                    .fit()
                    .centerCrop()
                    .noFade()
                    .into(userImage);
        }

        userName.setText(review.getUsername());
        reviewText.setText(review.getText());

        Picasso.get()
                .load(UIUtils.getRatingDrawableId(review.getRating()))
                .into(rating);
        reviewDate.setText(review.getTimeCreatedText());
    }

    @OnClick(R.id.parent)
    public void onReviewClicked() {
        listener.onReviewClicked(review);
    }
}
