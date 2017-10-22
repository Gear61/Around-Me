package com.randomappsinc.aroundme.activities;

import android.os.Bundle;
import android.view.View;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.models.Place;
import com.randomappsinc.aroundme.views.PlaceInfoView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceActivity extends StandardActivity {

    public static final String PLACE_KEY = "place";

    @BindView(R.id.parent) View mPlaceInfo;

    private Place mPlace;
    private PlaceInfoView mPlaceInfoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_view);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mPlace = getIntent().getParcelableExtra(PLACE_KEY);
        setTitle(mPlace.getName());

        mPlaceInfoView = new PlaceInfoView(
                this,
                mPlaceInfo,
                new IconDrawable(this, IoniconsIcons.ion_location).colorRes(R.color.dark_gray));
        mPlaceInfoView.loadPlace(mPlace);
    }
}
