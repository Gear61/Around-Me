package com.randomappsinc.aroundme.persistence.models;

import io.realm.RealmObject;

public class PlaceCategoryDO extends RealmObject {

    private String mAlias;
    private String mTitle;

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
}
