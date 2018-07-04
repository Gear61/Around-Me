package com.randomappsinc.aroundme.persistence.models;

import io.realm.RealmObject;

public class PlaceCategoryDO extends RealmObject {

    private String alias;
    private String title;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
