package com.randomappsinc.aroundme.utils;

import com.randomappsinc.aroundme.constants.DistanceUnit;

import java.util.Locale;

public class DistanceUtils {

    private static final double MILES_IN_A_METER = 0.000621371;
    private static final double METERS_IN_A_MILE = 1609.34;
    private static final double METERS_IN_A_KILOMETER = 1000.00;
    private static final double KILOMETERS_IN_A_METER = 0.001;

    public static double getMetersFromMiles(double miles) {
        return miles * METERS_IN_A_MILE;
    }

    public static double getMetersFromKilometers(double kilometers) {
        return kilometers * METERS_IN_A_KILOMETER;
    }

    public static double getMilesFromMeters(double meters) {
        return meters * MILES_IN_A_METER;
    }

    public static double getKilometersFromMeters(double meters) {
        return meters * KILOMETERS_IN_A_METER;
    }

    public static @DistanceUnit String getDefaultDistanceUnit() {
        return Locale.getDefault().equals(Locale.US)
                ? DistanceUnit.MILES
                : DistanceUnit.KILOMETERS;
    }
}
