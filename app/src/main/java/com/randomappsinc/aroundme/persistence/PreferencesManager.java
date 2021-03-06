package com.randomappsinc.aroundme.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.randomappsinc.aroundme.constants.DistanceUnit;
import com.randomappsinc.aroundme.models.Filter;
import com.randomappsinc.aroundme.utils.DistanceUtils;
import com.randomappsinc.aroundme.utils.MyApplication;

import java.util.Set;

public class PreferencesManager {

    private static final String FIRST_TIME_KEY = "firstTime";
    private static final String NUM_APP_OPENS = "numAppOpens";
    private static final int OPENS_BEFORE_RATING = 5;
    private static final String DISTANCE_UNIT_KEY = "distanceUnit";

    // Filter
    private static final String FILTER_RADIUS = "filterRadius";
    private static final String FILTER_SORT_TYPE = "filterSortType";
    private static final String FILTER_PRICE_RANGES = "filterPriceRanges";
    private static final String FILTER_ATTRIBUTES = "filterAttributes";

    private static PreferencesManager instance;

    public static PreferencesManager get() {
        if (instance == null) {
            instance = getSync();
        }
        return instance;
    }

    private static synchronized PreferencesManager getSync() {
        if (instance == null) {
            instance = new PreferencesManager();
        }
        return instance;
    }

    private SharedPreferences prefs;

    private PreferencesManager() {
        Context context = MyApplication.getAppContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean isFirstAppOpen() {
        boolean isFirstAppOpen = prefs.getBoolean(FIRST_TIME_KEY, true);
        prefs.edit().putBoolean(FIRST_TIME_KEY, false).apply();
        return isFirstAppOpen;
    }

    public boolean shouldAskForRating() {
        int currentAppOpens = prefs.getInt(NUM_APP_OPENS, 0);
        currentAppOpens++;
        prefs.edit().putInt(NUM_APP_OPENS, currentAppOpens).apply();
        return currentAppOpens == OPENS_BEFORE_RATING;
    }

    public Filter getFilter() {
        float radius = prefs.getFloat(FILTER_RADIUS, Filter.DEFAULT_RADIUS);
        String sortType = prefs.getString(FILTER_SORT_TYPE, Filter.DEFAULT_SORT_TYPE);
        Set<String> priceRanges = prefs.getStringSet(FILTER_PRICE_RANGES, Filter.DEFAULT_PRICE_RANGES);
        Set<String> attributes = prefs.getStringSet(FILTER_ATTRIBUTES, Filter.DEFAULT_ATTRIBUTES);

        Filter filter = new Filter();
        filter.setRadius(radius);
        filter.setSortType(sortType);
        filter.setPricesRanges(priceRanges);
        filter.setAttributes(attributes);
        return filter;
    }

    public void saveFilter(Filter filter) {
        prefs.edit()
                .putFloat(FILTER_RADIUS, filter.getRadius())
                .putString(FILTER_SORT_TYPE, filter.getSortType())
                .putStringSet(FILTER_PRICE_RANGES, filter.getPriceRanges())
                .putStringSet(FILTER_ATTRIBUTES, filter.getAttributes())
                .apply();
    }

    public @DistanceUnit String getDistanceUnit() {
        return prefs.getString(DISTANCE_UNIT_KEY, DistanceUtils.getDefaultDistanceUnit());
    }

    public void setDistanceUnit(@DistanceUnit String distanceUnit) {
        prefs.edit().putString(DISTANCE_UNIT_KEY, distanceUnit).apply();
    }
}
