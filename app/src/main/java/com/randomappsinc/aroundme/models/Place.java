package com.randomappsinc.aroundme.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable {

    private String id;
    private String name;
    private String imageUrl;
    private String phoneNumber;
    private String city;
    private String zipCode;
    private String country;
    private String state;
    private String address;
    private double latitude;
    private double longitude;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    protected Place(Parcel in) {
        id = in.readString();
        name = in.readString();
        imageUrl = in.readString();
        phoneNumber = in.readString();
        city = in.readString();
        zipCode = in.readString();
        country = in.readString();
        state = in.readString();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
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
        dest.writeString(phoneNumber);
        dest.writeString(city);
        dest.writeString(zipCode);
        dest.writeString(country);
        dest.writeString(state);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
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
