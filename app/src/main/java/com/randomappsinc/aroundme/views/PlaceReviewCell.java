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

    @BindView(R.id.user_image) CircleImageView mUserImage;
    @BindView(R.id.user_icon) View mUserIcon;
    @BindView(R.id.user_name) TextView mUserName;
    @BindView(R.id.review_text) TextView mReviewText;
    @BindView(R.id.rating) ImageView mRating;
    @BindView(R.id.review_date) TextView mReviewDate;

    private Listener mListener;
    private PlaceReview mReview;

    public PlaceReviewCell(View view, Listener listener) {
        ButterKnife.bind(this, view);
        mListener = listener;
    }

    public void loadReview(PlaceReview review, Context context) {
        mReview = review;

        Drawable defaultThumbnail = new IconDrawable(context, IoniconsIcons.ion_android_person)
                .colorRes(R.color.dark_gray);

        String userImageUrl = review.getUserImageUrl();
        if (userImageUrl == null || userImageUrl.isEmpty()) {
            mUserImage.setVisibility(View.GONE);
            mUserIcon.setVisibility(View.VISIBLE);
        } else {
            mUserIcon.setVisibility(View.GONE);
            mUserImage.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(review.getUserImageUrl())
                    .error(defaultThumbnail)
                    .fit()
                    .centerCrop()
                    .noFade()
                    .into(mUserImage);
        }

        mUserName.setText(review.getUsername());
        mReviewText.setText(review.getText());

        Picasso.with(context)
                .load(UIUtils.getRatingDrawableId(review.getRating()))
                .into(mRating);
        mReviewDate.setText(review.getTimeCreatedText());
    }

    @OnClick(R.id.parent)
    public void onReviewClicked() {
        mListener.onReviewClicked(mReview);
    }
}
