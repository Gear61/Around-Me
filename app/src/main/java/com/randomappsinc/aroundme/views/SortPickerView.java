package com.randomappsinc.aroundme.views;

import android.view.View;
import android.widget.CheckBox;

import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.models.Filter;
import com.randomappsinc.aroundme.models.SortType;
import com.randomappsinc.aroundme.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SortPickerView {

    @BindView(R.id.best_match_checkbox) CheckBox bestMatchCheckbox;
    @BindView(R.id.rating_checkbox) CheckBox ratingCheckbox;
    @BindView(R.id.review_count_checkbox) CheckBox reviewCountCheckbox;
    @BindView(R.id.distance_checkbox) CheckBox distanceCheckbox;

    public SortPickerView(View rootView) {
        ButterKnife.bind(this, rootView);
    }

    public @SortType String getChosenSortType() {
        if (ratingCheckbox.isChecked()) {
            return SortType.RATING;
        }
        if (reviewCountCheckbox.isChecked()) {
            return SortType.REVIEW_COUNT;
        }
        if (distanceCheckbox.isChecked()) {
            return SortType.DISTANCE;
        }
        return SortType.BEST_MATCH;
    }

    // Called when the filter page is opened and we need to load the data model into the view
    public void loadFilter(Filter filter) {
        @SortType String sortType = filter.getSortType();
        switch (sortType) {
            case SortType.BEST_MATCH:
                UIUtils.setCheckedImmediately(bestMatchCheckbox, true);
                break;
            case SortType.RATING:
                UIUtils.setCheckedImmediately(ratingCheckbox, true);
                break;
            case SortType.REVIEW_COUNT:
                UIUtils.setCheckedImmediately(reviewCountCheckbox, true);
                break;
            case SortType.DISTANCE:
                UIUtils.setCheckedImmediately(distanceCheckbox, true);
                break;
        }
    }

    @OnCheckedChanged(R.id.best_match_checkbox)
    public void onBestMatchSelected(boolean selected) {
        if (selected) {
            ratingCheckbox.setChecked(false);
            reviewCountCheckbox.setChecked(false);
            distanceCheckbox.setChecked(false);
        }
    }

    @OnCheckedChanged(R.id.rating_checkbox)
    public void onRatingSelected(boolean selected) {
        if (selected) {
            bestMatchCheckbox.setChecked(false);
            reviewCountCheckbox.setChecked(false);
            distanceCheckbox.setChecked(false);
        }
    }

    @OnCheckedChanged(R.id.review_count_checkbox)
    public void onReviewCountSelected(boolean selected) {
        if (selected) {
            bestMatchCheckbox.setChecked(false);
            ratingCheckbox.setChecked(false);
            distanceCheckbox.setChecked(false);
        }
    }

    @OnCheckedChanged(R.id.distance_checkbox)
    public void onDistanceSelected(boolean selected) {
        if (selected) {
            bestMatchCheckbox.setChecked(false);
            ratingCheckbox.setChecked(false);
            reviewCountCheckbox.setChecked(false);
        }
    }

    @OnClick(R.id.best_match_container)
    public void bestMatchClicked() {
        boolean isBestMatch = bestMatchCheckbox.isChecked();
        bestMatchCheckbox.setChecked(!isBestMatch);
    }

    @OnClick(R.id.rating_container)
    public void onRatingClicked() {
        boolean isRating = ratingCheckbox.isChecked();
        ratingCheckbox.setChecked(!isRating);
    }

    @OnClick(R.id.review_count_container)
    public void onReviewCountClicked() {
        boolean isReviewCount = reviewCountCheckbox.isChecked();
        reviewCountCheckbox.setChecked(!isReviewCount);
    }

    @OnClick(R.id.distance_container)
    public void onDistanceClicked() {
        boolean isDistance = distanceCheckbox.isChecked();
        distanceCheckbox.setChecked(!isDistance);
    }
}
