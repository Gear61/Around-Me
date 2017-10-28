package com.randomappsinc.aroundme.views;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.randomappsinc.aroundme.R;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BottomNavigationView {

    public interface Listener {
        void onNavItemSelected(@IdRes int viewId);

        void doVoiceSearch();
    }

    @BindView(R.id.home) TextView mHomeButton;
    @BindView(R.id.events) TextView mEventsButton;
    @BindView(R.id.favorites) TextView mFavoritesButton;
    @BindView(R.id.settings) TextView mSettingsButton;

    @BindColor(R.color.dark_gray) int darkGray;
    @BindColor(R.color.colorPrimary) int navyBlue;

    @NonNull
    private Listener mListener;
    private TextView mCurrentlySelected;

    public BottomNavigationView(View parent, @NonNull Listener listener) {
        ButterKnife.bind(this, parent);
        mListener = listener;

        mCurrentlySelected = mHomeButton;
        mHomeButton.setTextColor(navyBlue);
    }

    @OnClick(R.id.home)
    public void onHomeClicked() {
        if (mCurrentlySelected == mHomeButton) {
            return;
        }

        mCurrentlySelected.setTextColor(darkGray);
        mCurrentlySelected = mHomeButton;
        mHomeButton.setTextColor(navyBlue);
        mListener.onNavItemSelected(R.id.home);
    }

    @OnClick(R.id.events)
    public void onRestaurantsClicked() {
        if (mCurrentlySelected == mEventsButton) {
            return;
        }

        mCurrentlySelected.setTextColor(darkGray);
        mEventsButton.setTextColor(navyBlue);
        mCurrentlySelected = mEventsButton;
        mListener.onNavItemSelected(R.id.events);
    }

    @OnClick(R.id.microphone)
    public void doVoiceSearch() {
        mListener.doVoiceSearch();
    }

    @OnClick(R.id.favorites)
    public void onCheckInsClicked() {
        if (mCurrentlySelected == mFavoritesButton) {
            return;
        }

        mCurrentlySelected.setTextColor(darkGray);
        mFavoritesButton.setTextColor(navyBlue);
        mCurrentlySelected = mFavoritesButton;
        mListener.onNavItemSelected(R.id.favorites);
    }

    @OnClick(R.id.settings)
    public void onSettingsClicked() {
        if (mCurrentlySelected == mSettingsButton) {
            return;
        }

        mCurrentlySelected.setTextColor(darkGray);
        mSettingsButton.setTextColor(navyBlue);
        mCurrentlySelected = mSettingsButton;
        mListener.onNavItemSelected(R.id.settings);
    }
}
