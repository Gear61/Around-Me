package com.randomappsinc.aroundme.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.models.Filter;
import com.randomappsinc.aroundme.persistence.PreferencesManager;
import com.randomappsinc.aroundme.views.SortPickerView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterActivity extends AppCompatActivity {

    @BindView(R.id.filter_content) View filterContent;
    @BindView(R.id.radius_slider) SeekBar mRadiusSlider;
    @BindView(R.id.radius_text) TextView mDistanceText;

    @BindString(R.string.radius_text) String radiusTemplate;

    private Filter mFilter;
    private SortPickerView sortPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.filter);
        ButterKnife.bind(this);

        mRadiusSlider.setOnSeekBarChangeListener(mRadiusSliderListener);
        sortPickerView = new SortPickerView(filterContent);

        mFilter = PreferencesManager.get().getFilter();
        loadFilterIntoView();
    }

    private final SeekBar.OnSeekBarChangeListener mRadiusSliderListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            double progressAdjusted = (double) (progress + 1) / 10.0;
            mDistanceText.setText(String.format(radiusTemplate, progressAdjusted));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };

    private void loadFilterIntoView() {
        mRadiusSlider.setProgress((mFilter.getRadiusInMiles() * 10) - 1);
        sortPickerView.loadFilter(mFilter);
    }

    @OnClick(R.id.close)
    public void closeFilter() {
        finish();
    }

    @OnClick(R.id.reset_all)
    public void resetFilter() {
        mFilter.reset();
        loadFilterIntoView();
    }

    @OnClick(R.id.apply_filter)
    public void applyFilter() {
        PreferencesManager.get().saveFilter(mFilter);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_bottom);
    }
}
