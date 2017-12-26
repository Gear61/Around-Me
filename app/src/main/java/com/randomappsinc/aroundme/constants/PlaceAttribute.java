package com.randomappsinc.aroundme.constants;

import android.support.annotation.StringDef;

@StringDef({
    PlaceAttribute.HOT_AND_NEW,
    PlaceAttribute.GENDER_NEUTRAL_RESTROOMS
})
public @interface PlaceAttribute {
    String HOT_AND_NEW = "hot_and_new";
    String GENDER_NEUTRAL_RESTROOMS = "gender_neutral_restrooms";
}
