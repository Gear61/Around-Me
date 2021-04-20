package com.randomappsinc.aroundme.views;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

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

    @BindView(R.id.home) TextView homeButton;
    @BindView(R.id.events) TextView eventsButton;
    @BindView(R.id.favorites) TextView favoritesButton;
    @BindView(R.id.settings) TextView settingsButton;

    @BindColor(R.color.dark_gray) int darkGray;
    @BindColor(R.color.colorPrimary) int navyBlue;

    @NonNull private Listener listener;
    private TextView currentlySelected;

    public BottomNavigationView(View parent, @NonNull Listener listener) {
        ButterKnife.bind(this, parent);
        this.listener = listener;

        currentlySelected = homeButton;
        homeButton.setTextColor(navyBlue);
    }

    @OnClick(R.id.home)
    public void onHomeClicked() {
        if (currentlySelected == homeButton) {
            return;
        }

        currentlySelected.setTextColor(darkGray);
        currentlySelected = homeButton;
        homeButton.setTextColor(navyBlue);
        listener.onNavItemSelected(R.id.home);
    }

    @OnClick(R.id.events)
    public void onRestaurantsClicked() {
        if (currentlySelected == eventsButton) {
            return;
        }

        currentlySelected.setTextColor(darkGray);
        eventsButton.setTextColor(navyBlue);
        currentlySelected = eventsButton;
        listener.onNavItemSelected(R.id.events);
    }

    @OnClick(R.id.microphone)
    public void doVoiceSearch() {
        listener.doVoiceSearch();
    }

    @OnClick(R.id.favorites)
    public void onCheckInsClicked() {
        if (currentlySelected == favoritesButton) {
            return;
        }

        currentlySelected.setTextColor(darkGray);
        favoritesButton.setTextColor(navyBlue);
        currentlySelected = favoritesButton;
        listener.onNavItemSelected(R.id.favorites);
    }

    @OnClick(R.id.settings)
    public void onSettingsClicked() {
        if (currentlySelected == settingsButton) {
            return;
        }

        currentlySelected.setTextColor(darkGray);
        settingsButton.setTextColor(navyBlue);
        currentlySelected = settingsButton;
        listener.onNavItemSelected(R.id.settings);
    }
}
