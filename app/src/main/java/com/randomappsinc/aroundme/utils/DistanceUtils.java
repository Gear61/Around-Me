package com.randomappsinc.aroundme.utils;

import com.randomappsinc.aroundme.constants.DistanceUnit;

import java.util.Locale;

public class DistanceUtils {

    private static final double METER_TO_MILES = 0.000621371;
    private static final double MILES_TO_METERS = 1609.34;

    public static double getMetersFromMiles(double miles) {
        return miles * MILES_TO_METERS;
    }

    public static double getMilesFromMeters(double meters) {
        return meters * METER_TO_MILES;
    }

    public static @DistanceUnit String getDefaultDistanceUnit() {
        return Locale.getDefault().equals(Locale.US)
                ? DistanceUnit.MILES
                : DistanceUnit.KILOMETERS;
    }
}
