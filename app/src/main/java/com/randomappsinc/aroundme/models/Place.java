package com.randomappsinc.aroundme.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.randomappsinc.aroundme.persistence.models.PlaceCategoryDO;
import com.randomappsinc.aroundme.persistence.models.PlaceDO;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

public class Place implements Parcelable {

    private String id;
    private String name;
    private String imageUrl;
    private String url;
    private double rating;
    private int reviewCount;
    private String phoneNumber;
    private String price;
    private String city;
    private String zipCode;
    private String country;
    private String state;
    private String address;
    private double latitude;
    private double longitude;
    private boolean isFavorited;
    private List<PlaceCategory> categories;

    // Distance between the place location and the user's current location in miles/kilometers
    // Miles vs. kilometers is determined by the user's setting
    private double mDistance;

    public Place() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setIsFavorited(boolean isFavorited) {
        this.isFavorited = isFavorited;
    }

    public double getDistance() {
        return mDistance;
    }

    public void setDistance(double distance) {
        mDistance = distance;
    }

    public void setCategories(List<PlaceCategory> categories) {
        this.categories = categories;
    }

    public String getCategoriesListText() {
        StringBuilder categoriesList = new StringBuilder();
        for (PlaceCategory placeCategory : categories) {
            if (categoriesList.length() > 0) {
                categoriesList.append(", ");
            }
            categoriesList.append(placeCategory.getTitle());
        }
        return categoriesList.toString();
    }

    public void toggleFavorite() {
        isFavorited = !isFavorited;
    }

    public PlaceDO toPlaceDO() {
        PlaceDO placeDO = new PlaceDO();
        placeDO.setId(id);
        placeDO.setName(name);
        placeDO.setImageUrl(imageUrl);
        placeDO.setUrl(url);
        placeDO.setRating(rating);
        placeDO.setReviewCount(reviewCount);
        placeDO.setPhoneNumber(phoneNumber);
        placeDO.setPrice(price);
        placeDO.setCity(city);
        placeDO.setZipCode(zipCode);
        placeDO.setCountry(country);
        placeDO.setState(state);
        placeDO.setAddress(address);
        placeDO.setLatitude(latitude);
        placeDO.setLongitude(longitude);
        placeDO.setIsFavorited(isFavorited);
        RealmList<PlaceCategoryDO> categoryDOs = new RealmList<>();
        for (PlaceCategory placeCategory : categories) {
            categoryDOs.add(placeCategory.toPlaceCategoryDO());
        }
        placeDO.setCategories(categoryDOs);
        return placeDO;
    }

    protected Place(Parcel in) {
        id = in.readString();
        name = in.readString();
        imageUrl = in.readString();
        url = in.readString();
        rating = in.readDouble();
        reviewCount = in.readInt();
        phoneNumber = in.readString();
        price = in.readString();
        city = in.readString();
        zipCode = in.readString();
        country = in.readString();
        state = in.readString();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        isFavorited = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            categories = new ArrayList<>();
            in.readList(categories, PlaceCategory.class.getClassLoader());
        } else {
            categories = null;
        }
        mDistance = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeString(url);
        dest.writeDouble(rating);
        dest.writeInt(reviewCount);
        dest.writeString(phoneNumber);
        dest.writeString(price);
        dest.writeString(city);
        dest.writeString(zipCode);
        dest.writeString(country);
        dest.writeString(state);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeByte((byte) (isFavorited ? 0x01 : 0x00));
        if (categories == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(categories);
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
