package com.randomappsinc.aroundme.models;

import android.support.annotation.StringDef;

@StringDef({
    PriceRange.CHEAP,
    PriceRange.FAIR,
    PriceRange.PRICEY,
    PriceRange.VERY_EXPENSIVE
})
public @interface PriceRange {
    String CHEAP = "1";
    String FAIR = "2";
    String PRICEY = "3";
    String VERY_EXPENSIVE = "4";
}
