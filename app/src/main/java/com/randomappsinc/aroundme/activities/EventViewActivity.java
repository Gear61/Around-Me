package com.randomappsinc.aroundme.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.ShareCompat;

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
import com.randomappsinc.aroundme.persistence.DatabaseManager;
import com.randomappsinc.aroundme.utils.UIUtils;
import com.randomappsinc.aroundme.views.EventInfoView;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventViewActivity extends StandardActivity implements OnMapReadyCallback {

    public static final String EVENT_KEY = "event";

    @BindView(R.id.favorite_toggle) TextView favoriteToggle;
    @BindView(R.id.event_map) MapView eventMap;
    @BindView(R.id.event_info_parent) View eventInfo;
    @BindView(R.id.description_text) TextView descriptionText;
    @BindView(R.id.num_attending) TextView numAttending;
    @BindView(R.id.num_interested) TextView numInterested;
    @BindView(R.id.buy_tickets) View buyTicketsButton;

    @BindColor(R.color.light_red) int heartRed;
    @BindColor(R.color.dark_gray) int darkGray;

    private Event event;
    private EventInfoView eventInfoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        event = getIntent().getParcelableExtra(EVENT_KEY);
        setTitle(event.getName());

        eventMap.onCreate(savedInstanceState);
        eventMap.getMapAsync(this);

        // They could be opening up a favorited event from search
        event.setIsFavorited(DatabaseManager.get().getEventsDBManager().isEventFavorited(event));

        favoriteToggle.setText(event.isFavorited() ? R.string.heart_filled_icon : R.string.heart_icon);
        favoriteToggle.setTextColor(event.isFavorited() ? heartRed : darkGray);

        eventInfoView = new EventInfoView(
                eventInfo,
                new IconDrawable(this, IoniconsIcons.ion_android_calendar).colorRes(R.color.dark_gray));
        eventInfoView.loadEvent(event);

        descriptionText.setText(event.getDescriptionText());
        Linkify.addLinks(descriptionText, Linkify.ALL);
        numAttending.setText(String.valueOf(event.getNumAttending()));
        numInterested.setText(String.valueOf(event.getNumInterested()));

        if (TextUtils.isEmpty(event.getTicketsUrl())) {
            buyTicketsButton.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.favorite_toggle)
    public void onFavoriteClick() {
        event.toggleFavorite();
        UIUtils.animateFavoriteToggle(favoriteToggle, event.isFavorited());
        if (event.isFavorited()) {
            DatabaseManager.get().getEventsDBManager().addFavorite(event);
        } else {
            DatabaseManager.get().getEventsDBManager().removeFavorite(event);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.setOnMapClickListener(mMapClickListener);

        LatLng place = new LatLng(event.getLatitude(), event.getLongitude());
        googleMap.addMarker(new MarkerOptions()
                .position(place)
                .title(event.getName()));
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
            String mapUri = "google.navigation:q=" + event.getAddress() + " " + event.getName();
            startActivity(Intent.createChooser(
                    new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(mapUri)),
                    getString(R.string.navigate_with)));
        }
    };

    @OnClick(R.id.event_thumbnail)
    public void onThumbnailClicked() {
        if (TextUtils.isEmpty(event.getImageUrl())) {
            return;
        }

        Intent intent = new Intent(this, PictureFullViewActivity.class);
        ArrayList<String> imageUrl = new ArrayList<>();
        imageUrl.add(event.getImageUrl());
        intent.putStringArrayListExtra(PictureFullViewActivity.IMAGE_URLS_KEY, imageUrl);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @OnClick(R.id.description)
    public void openEventPage() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getUrl()));
        startActivity(intent);
    }

    @OnClick(R.id.add_to_calendar_button)
    public void addEventToCalendar() {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, event.getName());

        if (event.getTimeStart() > 0L) {
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.getTimeStart());
        }
        if (event.getTimeEnd() > 0L) {
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.getTimeEnd());
        }

        intent.putExtra(CalendarContract.Events.ALL_DAY, false);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, event.getDescription());
        startActivity(intent);
    }

    @OnClick(R.id.share_button)
    public void shareEvent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(event.getUrl())
                .getIntent();
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(shareIntent);
        }
    }

    @OnClick(R.id.buy_tickets)
    public void buyTickets() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getTicketsUrl()));
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        eventMap.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        eventMap.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        eventMap.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        eventMap.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        eventMap.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        eventMap.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventMap.onDestroy();
    }
}
