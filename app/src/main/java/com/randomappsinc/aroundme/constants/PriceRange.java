package com.randomappsinc.aroundme.constants;

import androidx.annotation.StringDef;

@StringDef({
    PriceRange.CHEAP,
    PriceRange.MODERATE,
    PriceRange.PRICEY,
    PriceRange.VERY_EXPENSIVE
})
public @interface PriceRange {
    String CHEAP = "1";
    String MODERATE = "2";
    String PRICEY = "3";
    String VERY_EXPENSIVE = "4";
}
