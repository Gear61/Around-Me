package com.randomappsinc.aroundme.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ShareCompat;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.models.Event;
import com.randomappsinc.aroundme.views.EventInfoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventViewActivity extends StandardActivity implements OnMapReadyCallback {

    public static final String EVENT_KEY = "event";

    @BindView(R.id.event_map) MapView mEventMap;
    @BindView(R.id.event_info_parent) View mEventInfo;
    @BindView(R.id.description_text) TextView mDescriptionText;
    @BindView(R.id.num_attending) TextView mNumAttending;
    @BindView(R.id.num_interested) TextView mNumInterested;
    @BindView(R.id.buy_tickets) View mBuyTicketsButton;

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

        mEventMap.onCreate(savedInstanceState);
        mEventMap.getMapAsync(this);

        mEventInfoView = new EventInfoView(
                this,
                mEventInfo,
                new IconDrawable(this, IoniconsIcons.ion_android_calendar).colorRes(R.color.dark_gray));
        mEventInfoView.loadEvent(mEvent);

        mDescriptionText.setText(mEvent.getDescriptionText());
        mNumAttending.setText(String.valueOf(mEvent.getNumAttending()));
        mNumInterested.setText(String.valueOf(mEvent.getNumInterested()));

        if (mEvent.getTicketsUrl() == null || mEvent.getTicketsUrl().isEmpty()) {
            mBuyTicketsButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mEventMap.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mEventMap.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mEventMap.onStart();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.setOnMapClickListener(mMapClickListener);

        LatLng place = new LatLng(mEvent.getLatitude(), mEvent.getLongitude());
        googleMap.addMarker(new MarkerOptions()
                .position(place)
                .title(mEvent.getName()));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(place)
                .zoom(16)
                .bearing(0)
                .tilt(0)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private final GoogleMap.OnMapClickListener mMapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            String mapUri = "google.navigation:q=" + mEvent.getAddress() + " " + mEvent.getName();
            startActivity(Intent.createChooser(
                    new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(mapUri)),
                    getString(R.string.navigate_with)));
        }
    };

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

    @OnClick(R.id.add_to_calendar_button)
    public void addEventToCalendar() {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, mEvent.getName());

        if (mEvent.getStartTimeMillis() > 0L) {
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, mEvent.getStartTimeMillis());
        }
        if (mEvent.getEndTimeMillis() > 0L) {
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, mEvent.getEndTimeMillis());
        }

        intent.putExtra(CalendarContract.Events.ALL_DAY, false);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, mEvent.getDescription());
        startActivity(intent);
    }

    @OnClick(R.id.share_button)
    public void shareEvent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mEvent.getUrl())
                .getIntent();
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(shareIntent);
        }
    }

    @OnClick(R.id.buy_tickets)
    public void buyTickets() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mEvent.getTicketsUrl()));
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        mEventMap.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mEventMap.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mEventMap.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mEventMap.onDestroy();
    }
}
