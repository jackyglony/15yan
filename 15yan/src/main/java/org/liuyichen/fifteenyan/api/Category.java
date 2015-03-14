package org.liuyichen.fifteenyan.api;

/**
 * Created by root on 15-3-12.
 */
public enum Category {

    LATEST("latest"), HOT("trending");

    private String v;

    private Category(String v) {
        this.v = v;
    }

    public String value() {
        return v;
    }
}
