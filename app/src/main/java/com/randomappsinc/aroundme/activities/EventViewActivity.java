package com.randomappsinc.aroundme.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
    @BindView(R.id.description_text) TextView mDescriptionText;
    @BindView(R.id.num_attending) TextView mNumAttending;
    @BindView(R.id.num_interested) TextView mNumInterested;

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

        mDescriptionText.setText(mEvent.getDescription());
        mNumAttending.setText(String.valueOf(mEvent.getNumAttending()));
        mNumInterested.setText(String.valueOf(mEvent.getNumInterested()));
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

    @OnClick(R.id.description)
    public void openEventPage() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mEvent.getUrl()));
        startActivity(intent);
    }
}
