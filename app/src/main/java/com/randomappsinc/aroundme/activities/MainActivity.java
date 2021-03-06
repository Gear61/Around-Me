package com.randomappsinc.aroundme.activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.IdRes;

import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.fragments.HomepageFragmentController;
import com.randomappsinc.aroundme.location.LocationManager;
import com.randomappsinc.aroundme.persistence.PreferencesManager;
import com.randomappsinc.aroundme.utils.StringUtils;
import com.randomappsinc.aroundme.utils.UIUtils;
import com.randomappsinc.aroundme.views.BottomNavigationView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends StandardActivity {

    private static final int SPEECH_REQUEST_CODE = 1;

    @BindView(R.id.bottom_navigation) View bottomNavigation;

    private final BottomNavigationView.Listener listener = new BottomNavigationView.Listener() {
        @Override
        public void onNavItemSelected(@IdRes int viewId) {
            navigationController.onNavItemSelected(viewId);
        }

        @Override
        public void doVoiceSearch() {
            showGoogleSpeechDialog();
        }
    };

    private HomepageFragmentController navigationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kill activity if it's above an existing stack due to launcher bug
        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null && getIntent().getAction().equals(Intent.ACTION_MAIN)) {
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        navigationController = new HomepageFragmentController(getSupportFragmentManager(), R.id.container);
        new BottomNavigationView(bottomNavigation, listener);
        navigationController.loadHome();

        if (PreferencesManager.get().shouldAskForRating()) {
            showRatingPrompt();
        }
    }

    private void showGoogleSpeechDialog() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_message));
        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } catch (ActivityNotFoundException exception) {
            Toast.makeText(
                    this,
                    R.string.speech_not_supported,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPEECH_REQUEST_CODE:
                if (resultCode != RESULT_OK || data == null) {
                    return;
                }

                List<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (result == null || result.isEmpty()) {
                    UIUtils.showToast(R.string.speech_unrecognized);
                    return;
                }
                String searchInput = StringUtils.capitalizeWords(result.get(0));
                Intent intent = new Intent(this, PlaceSearchActivity.class);
                intent.putExtra(PlaceSearchActivity.SEARCH_TERM_KEY, searchInput);
                startActivity(intent);
                break;
            case LocationManager.LOCATION_SERVICES_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    navigationController.alertEventsOfLocationServicesGrant();
                } else {
                    navigationController.alertEventsOfLocationServicesDenial();
                }
                break;
        }
    }

    private void showRatingPrompt() {
        new MaterialDialog.Builder(this)
                .content(R.string.please_rate)
                .negativeText(R.string.no_im_good)
                .positiveText(R.string.will_rate)
                .onPositive((dialog, which) -> {
                    Uri uri =  Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (!(getPackageManager().queryIntentActivities(intent, 0).size() > 0)) {
                        UIUtils.showToast(R.string.play_store_error);
                        return;
                    }
                    startActivity(intent);
                })
                .show();
    }
}
