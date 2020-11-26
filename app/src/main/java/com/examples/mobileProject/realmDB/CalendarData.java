package com.examples.mobileProject.realmDB;

import java.io.InputStream;
import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CalendarData extends RealmObject implements Serializable {
    private String contnet;
    @PrimaryKey
    private int date ;
    private Float negative;
    private Float positive;
    private byte [] img = null ;

    public void setImg(byte[] img) {
        this.img = img;
    }

    public byte[] getImg() {
        return img;
    }

    public String getContnet() {
        return contnet;
    }

    public void setContnet(String contnet) {
        this.contnet = contnet;
    }

    public int getDate() {
        return date;
    }
    public Float getNegative() {
        return negative;
    }

    public void setNegative(Float negative) {
        this.negative = negative;
    }

    public Float getPositive() {
        return positive;
    }

    public void setPositive(Float positive) {
        this.positive = positive;
    }
}
