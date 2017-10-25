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
import com.randomappsinc.aroundme.models.Review;
import com.randomappsinc.aroundme.utils.UIUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PlaceReviewsAdapter extends RecyclerView.Adapter<PlaceReviewsAdapter.PlaceReviewItemHolder> {

    public interface Listener {
        void onReviewClicked(Review review);
    }

    @NonNull private Listener mListener;
    private Context mContext;
    private List<Review> mReviews;
    private Drawable mDefaultThumbnail;

    public PlaceReviewsAdapter(Context context, @NonNull Listener listener) {
        mListener = listener;
        mContext = context;
        mReviews = new ArrayList<>();
        mDefaultThumbnail = new IconDrawable(mContext, IoniconsIcons.ion_android_person).colorRes(R.color.dark_gray);
    }

    public void setReviews(List<Review> reviews) {
        mReviews.clear();
        mReviews.addAll(reviews);
        notifyDataSetChanged();
    }

    @Override
    public PlaceReviewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.review_cell, parent, false);
        return new PlaceReviewItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaceReviewItemHolder holder, int position) {
        holder.loadReview(position);
    }

    @Override
    public int getItemCount() {
        return mReviews.isEmpty() ? 1 : mReviews.size();
    }

    class PlaceReviewItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.review_content) View mReviewContent;
        @BindView(R.id.user_image) CircleImageView mUserImage;
        @BindView(R.id.user_icon) View mUserIcon;
        @BindView(R.id.user_name) TextView mUserName;
        @BindView(R.id.review_text) TextView mReviewText;
        @BindView(R.id.rating) ImageView mRating;
        @BindView(R.id.no_reviews) View mNoReviews;

        PlaceReviewItemHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void loadReview(int position) {
            if (mReviews.isEmpty()) {
                mReviewContent.setVisibility(View.GONE);
                mNoReviews.setVisibility(View.VISIBLE);
            } else {
                mNoReviews.setVisibility(View.GONE);
                mReviewContent.setVisibility(View.VISIBLE);

                Review review = mReviews.get(position);

                String userImageUrl = review.getUser().getImageUrl();
                if (userImageUrl == null || userImageUrl.isEmpty()) {
                    mUserImage.setVisibility(View.GONE);
                    mUserIcon.setVisibility(View.VISIBLE);
                } else {
                    mUserIcon.setVisibility(View.GONE);
                    mUserImage.setVisibility(View.VISIBLE);
                    Picasso.with(mContext)
                            .load(review.getUser().getImageUrl())
                            .error(mDefaultThumbnail)
                            .fit()
                            .centerCrop()
                            .noFade()
                            .into(mUserImage);
                }

                mUserName.setText(review.getUser().getName());
                mReviewText.setText(review.getText());

                Picasso.with(mContext)
                        .load(UIUtils.getRatingDrawableId(review.getRating()))
                        .into(mRating);
            }
        }

        @OnClick(R.id.parent)
        public void onReviewClicked() {
            Review review = mReviews.get(getAdapterPosition());
            mListener.onReviewClicked(review);
        }
    }
}
