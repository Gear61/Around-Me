package com.randomappsinc.aroundme.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.randomappsinc.aroundme.persistence.models.PlaceCategoryDO;
import com.randomappsinc.aroundme.persistence.models.PlaceDO;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

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
    private boolean mIsFavorited;
    private List<PlaceCategory> mCategories;

    // Distance between the place location and the user's current location in miles/kilometers
    // Miles vs. kilometers is determined by the user's setting
    private double mDistance;

    public Place() {}

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

    public void setCity(String city) {
        mCity = city;
    }

    public void setZipCode(String zipCode) {
        mZipCode = zipCode;
    }

    public void setCountry(String country) {
        mCountry = country;
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

    public double getDistance() {
        return mDistance;
    }

    public void setDistance(double distance) {
        mDistance = distance;
    }

    public void setCategories(List<PlaceCategory> categories) {
        mCategories = categories;
    }

    public String getCategoriesListText() {
        StringBuilder categoriesList = new StringBuilder();
        for (PlaceCategory placeCategory : mCategories) {
            if (categoriesList.length() > 0) {
                categoriesList.append(", ");
            }
            categoriesList.append(placeCategory.getTitle());
        }
        return categoriesList.toString();
    }

    public void toggleFavorite() {
        mIsFavorited = !mIsFavorited;
    }

    public PlaceDO toPlaceDO() {
        PlaceDO placeDO = new PlaceDO();
        placeDO.setId(mId);
        placeDO.setName(mName);
        placeDO.setImageUrl(mImageUrl);
        placeDO.setUrl(mUrl);
        placeDO.setRating(mRating);
        placeDO.setReviewCount(mReviewCount);
        placeDO.setPhoneNumber(mPhoneNumber);
        placeDO.setPrice(mPrice);
        placeDO.setCity(mCity);
        placeDO.setZipCode(mZipCode);
        placeDO.setCountry(mCountry);
        placeDO.setState(mState);
        placeDO.setAddress(mAddress);
        placeDO.setLatitude(mLatitude);
        placeDO.setLongitude(mLongitude);
        placeDO.setIsFavorited(mIsFavorited);
        RealmList<PlaceCategoryDO> categoryDOs = new RealmList<>();
        for (PlaceCategory placeCategory : mCategories) {
            categoryDOs.add(placeCategory.toPlaceCategoryDO());
        }
        placeDO.setCategories(categoryDOs);
        return placeDO;
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
        mIsFavorited = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            mCategories = new ArrayList<>();
            in.readList(mCategories, PlaceCategory.class.getClassLoader());
        } else {
            mCategories = null;
        }
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
        dest.writeByte((byte) (mIsFavorited ? 0x01 : 0x00));
        if (mCategories == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mCategories);
        }
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
