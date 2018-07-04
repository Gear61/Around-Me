package com.randomappsinc.aroundme.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.randomappsinc.aroundme.persistence.models.PlaceCategoryDO;

public class PlaceCategory implements Parcelable {

    private String alias;
    private String title;

    public PlaceCategory() {}

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PlaceCategoryDO toPlaceCategoryDO() {
        PlaceCategoryDO placeCategoryDO = new PlaceCategoryDO();
        placeCategoryDO.setAlias(alias);
        placeCategoryDO.setTitle(title);
        return placeCategoryDO;
    }

    protected PlaceCategory(Parcel in) {
        alias = in.readString();
        title = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(alias);
        dest.writeString(title);
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
