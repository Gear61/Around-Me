package com.randomappsinc.aroundme.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.adapters.PlaceTypesAdapter;
import com.randomappsinc.aroundme.dialogs.PlaceTypeAdder;
import com.randomappsinc.aroundme.persistence.PreferencesManager;
import com.randomappsinc.aroundme.utils.MyApplication;
import com.randomappsinc.aroundme.utils.SimpleDividerItemDecoration;
import com.randomappsinc.aroundme.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends StandardActivity implements PlaceTypesAdapter.Listener {

    @BindView(R.id.parent) View mParent;
    @BindView(R.id.place_types) RecyclerView mPlaceTypes;
    @BindView(R.id.add_place_type) FloatingActionButton mAddPlaceType;

    private PlaceTypeAdder mPlaceTypeAdder;
    private PlaceTypesAdapter mPlaceTypesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAddPlaceType.setImageDrawable(
                new IconDrawable(this, IoniconsIcons.ion_android_add).colorRes(R.color.white));

        mPlaceTypes.addItemDecoration(new SimpleDividerItemDecoration(this));
        mPlaceTypesAdapter = new PlaceTypesAdapter(this, this, mParent);
        mPlaceTypes.setAdapter(mPlaceTypesAdapter);

        mPlaceTypeAdder = new PlaceTypeAdder(this, mTypeAddedListener);

        if (PreferencesManager.get().shouldAskForRating()) {
            showRatingPrompt();
        }
    }

    @OnClick(R.id.add_place_type)
    public void addPlaceType() {
        mPlaceTypeAdder.show();
    }

    private final PlaceTypeAdder.Listener mTypeAddedListener = new PlaceTypeAdder.Listener() {
        @Override
        public void onPlaceTypeAdded() {
            mPlaceTypesAdapter.updateWithAdded();
            UIUtils.showSnackbar(mParent, R.string.place_type_added);
        }
    };

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void scrollToItem(int position) {
        mPlaceTypes.smoothScrollToPosition(position);
    }

    private void showRatingPrompt() {
        new MaterialDialog.Builder(this)
                .content(R.string.please_rate)
                .negativeText(R.string.no_im_good)
                .positiveText(R.string.will_rate)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Uri uri =  Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        if (!(getPackageManager().queryIntentActivities(intent, 0).size() > 0)) {
                            Toast.makeText(
                                    MyApplication.getAppContext(),
                                    R.string.play_store_error,
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        startActivity(intent);
                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        UIUtils.loadMenuIcon(menu, R.id.settings, IoniconsIcons.ion_android_settings, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
