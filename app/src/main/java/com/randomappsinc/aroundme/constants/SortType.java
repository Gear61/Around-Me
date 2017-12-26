package com.randomappsinc.aroundme.constants;

import android.support.annotation.StringDef;

@StringDef({
    SortType.BEST_MATCH,
    SortType.RATING,
    SortType.REVIEW_COUNT,
    SortType.DISTANCE
})
public @interface SortType {
    String BEST_MATCH = "best_match";
    String RATING = "rating";
    String REVIEW_COUNT = "review_count";
    String DISTANCE = "distance";
}
