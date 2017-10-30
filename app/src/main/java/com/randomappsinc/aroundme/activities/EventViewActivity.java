package com.randomappsinc.aroundme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.models.Event;
import com.randomappsinc.aroundme.views.EventInfoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventViewActivity extends StandardActivity {

    public static final String EVENT_KEY = "event";

    @BindView(R.id.event_info_parent) View mEventInfo;

    private Event mEvent;
    private EventInfoView mEventInfoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mEvent = getIntent().getParcelableExtra(EVENT_KEY);
        setTitle(mEvent.getName());

        mEventInfoView = new EventInfoView(
                this,
                mEventInfo,
                new IconDrawable(this, IoniconsIcons.ion_android_calendar).colorRes(R.color.dark_gray));
        mEventInfoView.loadEvent(mEvent);
    }

    @OnClick(R.id.event_thumbnail)
    public void onThumbnailClicked() {
        Intent intent = new Intent(this, PictureFullViewActivity.class);
        ArrayList<String> imageUrl = new ArrayList<>();
        imageUrl.add(mEvent.getImageUrl());
        intent.putStringArrayListExtra(PictureFullViewActivity.IMAGE_URLS_KEY, imageUrl);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
