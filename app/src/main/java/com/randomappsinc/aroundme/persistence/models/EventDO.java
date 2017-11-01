package com.randomappsinc.aroundme.persistence.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EventDO extends RealmObject {

    @PrimaryKey
    private String mId;

    private String mImageUrl;
    private String mName;
    private int mNumAttending;
    private int mNumInterested;
    private double mCost;
    private double mCostMax;
    private String mDescription;
    private String mUrl;
    private boolean mIsCanceled;
    private boolean mIsFree;
    private String mTicketsUrl;
    private String mTimeStart;
    private String mTimeEnd;
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

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getNumAttending() {
        return mNumAttending;
    }

    public void setNumAttending(int numAttending) {
        mNumAttending = numAttending;
    }

    public int getNumInterested() {
        return mNumInterested;
    }

    public void setNumInterested(int numInterested) {
        mNumInterested = numInterested;
    }

    public double getCost() {
        return mCost;
    }

    public void setCost(double cost) {
        mCost = cost;
    }

    public double getCostMax() {
        return mCostMax;
    }

    public void setCostMax(double costMax) {
        mCostMax = costMax;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public boolean isCanceled() {
        return mIsCanceled;
    }

    public void setIsCanceled(boolean isCanceled) {
        mIsCanceled = isCanceled;
    }

    public boolean isFree() {
        return mIsFree;
    }

    public void setIsFree(boolean isFree) {
        mIsFree = isFree;
    }

    public String getTicketsUrl() {
        return mTicketsUrl;
    }

    public void setTicketsUrl(String ticketsUrl) {
        mTicketsUrl = ticketsUrl;
    }

    public String getTimeStart() {
        return mTimeStart;
    }

    public void setTimeStart(String timeStart) {
        mTimeStart = timeStart;
    }

    public String getTimeEnd() {
        return mTimeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        mTimeEnd = timeEnd;
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

    public boolean iFavorited() {
        return mIsFavorited;
    }

    public void setIsFavorited(boolean isFavorited) {
        mIsFavorited = isFavorited;
    }
}
