package com.randomappsinc.aroundme.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.constants.DistanceUnit;
import com.randomappsinc.aroundme.models.Filter;
import com.randomappsinc.aroundme.persistence.PreferencesManager;
import com.randomappsinc.aroundme.views.AttributePickerView;
import com.randomappsinc.aroundme.views.PriceRangePickerView;
import com.randomappsinc.aroundme.views.SortPickerView;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterActivity extends AppCompatActivity {

    @BindView(R.id.filter_content) View filterContent;
    @BindView(R.id.radius_slider) SeekBar radiusSlider;
    @BindView(R.id.radius_text) TextView distanceText;

    @BindInt(R.integer.max_radius_volume_miles) int maxMilesSliderValue;
    @BindInt(R.integer.max_radius_volume_kilometers) int maxKilometersSliderValue;

    @BindString(R.string.radius_text_miles) String radiusTemplateMiles;
    @BindString(R.string.radius_text_kilometers) String radiusTemplateKilometers;

    private Filter filter;
    private SortPickerView sortPickerView;
    private PriceRangePickerView priceRangePickerView;
    private AttributePickerView attributePickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.filter);
        ButterKnife.bind(this);

        radiusSlider.setMax(PreferencesManager.get().getDistanceUnit().equals(DistanceUnit.MILES)
                ? maxMilesSliderValue
                : maxKilometersSliderValue);

        sortPickerView = new SortPickerView(filterContent);
        priceRangePickerView = new PriceRangePickerView(filterContent);
        attributePickerView = new AttributePickerView(filterContent);

        filter = PreferencesManager.get().getFilter();
        loadFilterIntoView();

        radiusSlider.setOnSeekBarChangeListener(mRadiusSliderListener);
    }

    private final SeekBar.OnSeekBarChangeListener mRadiusSliderListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            setDistanceSliderText(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };

    private void setDistanceSliderText(int sliderValue) {
        double progressAdjusted = (double) (sliderValue + 1) / 10.0;
        String template = PreferencesManager.get().getDistanceUnit().equals(DistanceUnit.MILES)
                ? radiusTemplateMiles
                : radiusTemplateKilometers;
        distanceText.setText(String.format(template, progressAdjusted));
    }

    private void loadFilterIntoView() {
        float filterDistanceValue = PreferencesManager.get().getDistanceUnit().equals(DistanceUnit.MILES)
                ? filter.getRadiusInMiles()
                : filter.getRadiusInKilometers();

        // 0.1km doesn't convert to 0.1 miles, so we need to have a safeguard to prevent negatives
        int convertedSliderValue = Math.round(Math.max((filterDistanceValue * 10) - 1, 0));
        radiusSlider.setProgress(convertedSliderValue);
        setDistanceSliderText(convertedSliderValue);

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
        double distanceValue = (sliderVal + 1) / 10;

        if (PreferencesManager.get().getDistanceUnit().equals(DistanceUnit.MILES)) {
            filter.setRadiusWithMiles(distanceValue);
        } else {
            filter.setRadiusWithKilometers(distanceValue);
        }
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
