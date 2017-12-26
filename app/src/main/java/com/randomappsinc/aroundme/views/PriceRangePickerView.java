package com.randomappsinc.aroundme.views;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.constants.PriceRange;
import com.randomappsinc.aroundme.models.Filter;
import com.randomappsinc.aroundme.utils.StringUtils;
import com.randomappsinc.aroundme.utils.UIUtils;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PriceRangePickerView {

    @BindView(R.id.cheap_text) TextView cheapText;
    @BindView(R.id.moderate_text) TextView moderateText;
    @BindView(R.id.pricey_text) TextView priceyText;
    @BindView(R.id.very_expensive_text) TextView veryExpensiveText;

    @BindView(R.id.cheap_checkbox) CheckBox cheapCheckbox;
    @BindView(R.id.moderate_checkbox) CheckBox moderateCheckbox;
    @BindView(R.id.pricey_checkbox) CheckBox priceyCheckbox;
    @BindView(R.id.very_expensive_checkbox) CheckBox veryExpensiveCheckbox;

    public PriceRangePickerView(View rootView) {
        ButterKnife.bind(this, rootView);
        initPriceDescriptors();
    }

    public Set<String> getPriceRanges() {
        Set<String> priceRanges = new HashSet<>();
        if (cheapCheckbox.isChecked()) {
            priceRanges.add(PriceRange.CHEAP);
        }
        if (moderateCheckbox.isChecked()) {
            priceRanges.add(PriceRange.MODERATE);
        }
        if (priceyCheckbox.isChecked()) {
            priceRanges.add(PriceRange.PRICEY);
        }
        if (veryExpensiveCheckbox.isChecked()) {
            priceRanges.add(PriceRange.VERY_EXPENSIVE);
        }
        return priceRanges;
    }

    private void initPriceDescriptors() {
        String curSymbol = StringUtils.getCurrencySymbol();

        String cheap = StringUtils.getString(R.string.cheap);
        String cheapDescriptor = cheap + " (" + curSymbol + ")";
        cheapText.setText(cheapDescriptor);

        String moderate = StringUtils.getString(R.string.moderate);
        String moderateDescriptor = moderate + " (" + curSymbol + curSymbol + ")";
        moderateText.setText(moderateDescriptor);

        String pricey = StringUtils.getString(R.string.pricey);
        String priceyDescriptor = pricey + " (" + curSymbol + curSymbol + curSymbol + ")";
        priceyText.setText(priceyDescriptor);

        String veryExpensive = StringUtils.getString(R.string.very_expensive);
        String veryExpensiveDescriptor = veryExpensive
                + " (" + curSymbol + curSymbol + curSymbol + curSymbol + ")";
        veryExpensiveText.setText(veryExpensiveDescriptor);
    }

    public void loadFilter(Filter filter) {
        Set<String> priceRanges = filter.getPriceRanges();
        boolean cheapSelected = priceRanges.contains(PriceRange.CHEAP);
        UIUtils.setCheckedImmediately(cheapCheckbox, cheapSelected);

        boolean moderateSelected = priceRanges.contains(PriceRange.MODERATE);
        UIUtils.setCheckedImmediately(moderateCheckbox, moderateSelected);

        boolean priceySelected = priceRanges.contains(PriceRange.PRICEY);
        UIUtils.setCheckedImmediately(priceyCheckbox, priceySelected);

        boolean veryExpensiveSelected = priceRanges.contains(PriceRange.VERY_EXPENSIVE);
        UIUtils.setCheckedImmediately(veryExpensiveCheckbox, veryExpensiveSelected);
    }

    @OnClick(R.id.cheap_container)
    public void onCheapClicked() {
        boolean cheapSelected = cheapCheckbox.isChecked();
        cheapCheckbox.setChecked(!cheapSelected);
    }

    @OnClick(R.id.moderate_container)
    public void onModerateClicked() {
        boolean moderateSelected = moderateCheckbox.isChecked();
        moderateCheckbox.setChecked(!moderateSelected);
    }

    @OnClick(R.id.pricey_container)
    public void onPriceyClicked() {
        boolean priceySelected = priceyCheckbox.isChecked();
        priceyCheckbox.setChecked(!priceySelected);
    }

    @OnClick(R.id.very_expensive_container)
    public void onVeryExpensiveClicked() {
        boolean veryExpensiveSelected = veryExpensiveCheckbox.isChecked();
        veryExpensiveCheckbox.setChecked(!veryExpensiveSelected);
    }
}
