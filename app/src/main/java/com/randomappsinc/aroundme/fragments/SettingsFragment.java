package com.randomappsinc.aroundme.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.adapters.SettingsAdapter;
import com.randomappsinc.aroundme.utils.SimpleDividerItemDecoration;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SettingsFragment extends Fragment implements SettingsAdapter.ItemSelectionListener {

    public static final String SUPPORT_EMAIL = "chessnone@gmail.com";
    public static final String OTHER_APPS_URL = "https://play.google.com/store/apps/dev?id=9093438553713389916";
    public static final String REPO_URL = "https://github.com/Gear61/Around-Me";

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.settings_options) RecyclerView settingsOptions;
    @BindString(R.string.feedback_subject) String feedbackSubject;
    @BindString(R.string.send_email) String sendEmail;

    private Unbinder mUnbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        mToolbar.setTitle(R.string.settings);
        settingsOptions.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        settingsOptions.setAdapter(new SettingsAdapter(getActivity(), this));
        return rootView;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = null;
        switch (position) {
            case 0:
                String uriText = "mailto:" + SUPPORT_EMAIL + "?subject=" + Uri.encode(feedbackSubject);
                Uri mailUri = Uri.parse(uriText);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, mailUri);
                getActivity().startActivity(Intent.createChooser(sendIntent, sendEmail));
                return;
            case 1:
                Intent shareIntent = ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(getString(R.string.share_app_message))
                        .getIntent();
                if (shareIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    getActivity().startActivity(shareIntent);
                }
                return;
            case 2:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(OTHER_APPS_URL));
                break;
            case 3:
                Uri uri =  Uri.parse("market://details?id=" + getActivity().getPackageName());
                intent = new Intent(Intent.ACTION_VIEW, uri);
                if (!(getActivity().getPackageManager().queryIntentActivities(intent, 0).size() > 0)) {
                    Toast.makeText(getActivity(), R.string.play_store_error, Toast.LENGTH_LONG).show();
                    return;
                }
                break;
            case 4:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(REPO_URL));
                break;
        }
        getActivity().startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
