package com.randomappsinc.aroundme.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.randomappsinc.aroundme.persistence.models.PlaceCategoryDO;

public class PlaceCategory implements Parcelable {

    private String mAlias;
    private String mTitle;

    public PlaceCategory() {}

    public String getAlias() {
        return mAlias;
    }

    public void setAlias(String alias) {
        mAlias = alias;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public PlaceCategoryDO toPlaceCategoryDO() {
        PlaceCategoryDO placeCategoryDO = new PlaceCategoryDO();
        placeCategoryDO.setAlias(mAlias);
        placeCategoryDO.setTitle(mTitle);
        return placeCategoryDO;
    }

    protected PlaceCategory(Parcel in) {
        mAlias = in.readString();
        mTitle = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAlias);
        dest.writeString(mTitle);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PlaceCategory> CREATOR = new Parcelable.Creator<PlaceCategory>() {
        @Override
        public PlaceCategory createFromParcel(Parcel in) {
            return new PlaceCategory(in);
        }

        @Override
        public PlaceCategory[] newArray(int size) {
            return new PlaceCategory[size];
        }
    };
}
