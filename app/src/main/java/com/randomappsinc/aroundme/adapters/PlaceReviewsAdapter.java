package com.randomappsinc.aroundme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.models.PlaceReview;
import com.randomappsinc.aroundme.views.PlaceReviewCell;

import java.util.List;

public class PlaceReviewsAdapter implements PlaceReviewCell.Listener {

    public interface Listener {
        void onReviewClicked(PlaceReview review);
    }

    @NonNull private Listener listener;

    public PlaceReviewsAdapter(@NonNull Listener listener) {
        this.listener = listener;
    }

    public void setReviews(List<PlaceReview> reviews, ViewGroup reviewContainer, Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (reviews.isEmpty()) {
            View noReviewsCell = inflater.inflate(
                    R.layout.no_reviews_cell,
                    reviewContainer,
                    false);
            reviewContainer.addView(noReviewsCell);
        } else {
            for (PlaceReview review : reviews) {
                View reviewCellParent = inflater.inflate(
                        R.layout.review_cell,
                        reviewContainer,
                        false);
                PlaceReviewCell placeReviewCell = new PlaceReviewCell(reviewCellParent, this);
                placeReviewCell.loadReview(review, context);
                reviewContainer.addView(reviewCellParent);
            }
        }
    }

    @Override
    public void onReviewClicked(PlaceReview placeReview) {
        listener.onReviewClicked(placeReview);
    }
}
