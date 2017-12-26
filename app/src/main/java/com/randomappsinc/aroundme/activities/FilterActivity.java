package com.randomappsinc.aroundme.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.models.Filter;
import com.randomappsinc.aroundme.persistence.PreferencesManager;
import com.randomappsinc.aroundme.views.AttributePickerView;
import com.randomappsinc.aroundme.views.PriceRangePickerView;
import com.randomappsinc.aroundme.views.SortPickerView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterActivity extends AppCompatActivity {

    @BindView(R.id.filter_content) View filterContent;
    @BindView(R.id.radius_slider) SeekBar radiusSlider;
    @BindView(R.id.radius_text) TextView distanceText;

    @BindString(R.string.radius_text) String radiusTemplate;

    private Filter filter;
    private SortPickerView sortPickerView;
    private PriceRangePickerView priceRangePickerView;
    private AttributePickerView attributePickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.filter);
        ButterKnife.bind(this);

        radiusSlider.setOnSeekBarChangeListener(mRadiusSliderListener);
        sortPickerView = new SortPickerView(filterContent);
        priceRangePickerView = new PriceRangePickerView(filterContent);
        attributePickerView = new AttributePickerView(filterContent);

        filter = PreferencesManager.get().getFilter();
        loadFilterIntoView();
    }

    private final SeekBar.OnSeekBarChangeListener mRadiusSliderListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            double progressAdjusted = (double) (progress + 1) / 10.0;
            distanceText.setText(String.format(radiusTemplate, progressAdjusted));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };

    private void loadFilterIntoView() {
        float convertedSliderValue = (filter.getRadiusInMiles() * 10) - 1;
        radiusSlider.setProgress(Math.round(convertedSliderValue));
        sortPickerView.loadFilter(filter);
        priceRangePickerView.loadFilter(filter);
        attributePickerView.loadFilter(filter);
    }

    @OnClick(R.id.close)
    public void closeFilter() {
        finish();
    }

    @OnClick(R.id.reset_all)
    public void resetFilter() {
        filter.reset();
        loadFilterIntoView();
    }

    @OnClick(R.id.apply_filter)
    public void applyFilter() {
        double sliderVal = radiusSlider.getProgress();
        double miles = (sliderVal + 1) / 10;
        filter.setRadiusWithMiles(miles);
        filter.setSortType(sortPickerView.getChosenSortType());
        filter.setPricesRanges(priceRangePickerView.getPriceRanges());
        filter.setAttributes(attributePickerView.getAttributes());
        PreferencesManager.get().saveFilter(filter);
        Toast.makeText(this, R.string.filter_applied, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_bottom);
    }
}
