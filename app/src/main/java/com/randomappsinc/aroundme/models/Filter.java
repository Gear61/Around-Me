package com.randomappsinc.aroundme.models;

import com.randomappsinc.aroundme.utils.DistanceUtils;

import java.util.HashSet;
import java.util.Set;

public class Filter {

    public static final int DEFAULT_RADIUS = 8047;
    public static final String DEFAULT_SORT = SortType.BEST_MATCH;
    public static final Set<String> DEFAULT_PRICE_RANGES = new HashSet<>();

    private static final double MAX_YELP_DISTANCE = 40000;

    // Allowed distance from the user's current location in meters
    private int radius;
    private @SortType String sortType;
    private Set<String> priceRanges;

    public int getRadius() {
        return radius;
    }

    public @SortType String getSortType() {
        return sortType;
    }

    public Set<String> getPriceRanges() {
        return priceRanges;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setRadiusWithMiles(double miles) {
        double converted = DistanceUtils.getMilesFromMeters(miles);
        radius = (int) Math.max(converted, MAX_YELP_DISTANCE);
    }

    public void setSortType(@SortType String sortType) {
        this.sortType = sortType;
    }

    public void clearPriceRanges() {
        priceRanges.clear();
    }

    public void addPriceRange(@PriceRange String priceRange) {
        priceRanges.add(priceRange);
    }

    public void setPricesRanges(Set<String> priceRanges) {
        this.priceRanges = priceRanges;
    }
}
