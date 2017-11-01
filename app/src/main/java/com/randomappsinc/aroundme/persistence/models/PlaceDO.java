package com.randomappsinc.aroundme.persistence.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PlaceDO extends RealmObject {

    @PrimaryKey
    private String mId;

    private String mName;
    private String mImageUrl;
    private String mUrl;
    private double mRating;
    private int mReviewCount;
    private String mPhoneNumber;
    private String mPrice;
    private String mCity;
    private String mZipCode;
    private String mCountry;
    private String mState;
    private String mAddress;
    private double mLatitude;
    private double mLongitude;
    private boolean mIsFavorited;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(double rating) {
        mRating = rating;
    }

    public int getReviewCount() {
        return mReviewCount;
    }

    public void setReviewCount(int reviewCount) {
        mReviewCount = reviewCount;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getZipCode() {
        return mZipCode;
    }

    public void setZipCode(String zipCode) {
        mZipCode = zipCode;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public boolean isFavorited() {
        return mIsFavorited;
    }

    public void setIsFavorited(boolean isFavorited) {
        mIsFavorited = isFavorited;
    }
}
