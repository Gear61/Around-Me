package com.randomappsinc.aroundme.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable {

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

    // Distance from the search location in miles
    private double mDistance;

    public Place() {}

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(double rating) {
        this.mRating = rating;
    }

    public int getReviewCount() {
        return mReviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.mReviewCount = reviewCount;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public void setCity(String city) {
        this.mCity = city;
    }

    public void setZipCode(String zipCode) {
        this.mZipCode = zipCode;
    }

    public void setCountry(String country) {
        this.mCountry = country;
    }

    public void setState(String state) {
        this.mState = state;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    public double getDistance() {
        return mDistance;
    }

    public void setDistance(double distance) {
        this.mDistance = distance;
    }

    protected Place(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mImageUrl = in.readString();
        mUrl = in.readString();
        mRating = in.readDouble();
        mReviewCount = in.readInt();
        mPhoneNumber = in.readString();
        mPrice = in.readString();
        mCity = in.readString();
        mZipCode = in.readString();
        mCountry = in.readString();
        mState = in.readString();
        mAddress = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mDistance = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mImageUrl);
        dest.writeString(mUrl);
        dest.writeDouble(mRating);
        dest.writeInt(mReviewCount);
        dest.writeString(mPhoneNumber);
        dest.writeString(mPrice);
        dest.writeString(mCity);
        dest.writeString(mZipCode);
        dest.writeString(mCountry);
        dest.writeString(mState);
        dest.writeString(mAddress);
        dest.writeDouble(mLatitude);
        dest.writeDouble(mLongitude);
        dest.writeDouble(mDistance);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
}
