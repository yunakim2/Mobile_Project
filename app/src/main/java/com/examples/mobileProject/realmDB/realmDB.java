package com.examples.mobileProject.realmDB;

import android.graphics.Bitmap;

import java.sql.Time;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class realmDB {
    Realm realm;

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    public RealmResults<CalendarData> getDiaryAllList() {
        RealmResults<CalendarData> diaryRealmResults = realm.where(CalendarData.class).findAll();
        return diaryRealmResults;
    }

    public CalendarData getDiary(int Date) {
        CalendarData data =  realm.where(CalendarData.class).equalTo("date",Date).findFirst();
        return data;
    }

    public RealmResults<CalendarData> getWeekDiaryList(int startDate, int endDate) {
        RealmResults<CalendarData> data = realm.where(CalendarData.class).greaterThan("date",startDate).lessThanOrEqualTo("date",endDate).findAll();
        return data;
    }
    public int getWeekDiaryListCount(int startDate, int endDate) {
        RealmResults<CalendarData> data = realm.where(CalendarData.class).greaterThan("date",startDate).lessThanOrEqualTo("date",endDate).findAll();
        return data.size();
    }

    public boolean updateDiary(int Date, String Content, Float Pos, Float Neg, byte[] img) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CalendarData calendarData = realm.where(CalendarData.class).equalTo("date",Date).findFirst();
                // insert
                if(calendarData==null) {
                    CalendarData newdata = realm.createObject(CalendarData.class, Date);
                    newdata.setContnet(Content);
                    newdata.setPositive(Pos);
                    newdata.setNegative(Neg);
                    newdata.setImg(img);

                } else {
                    //update
                    calendarData.setContnet(Content);
                    calendarData.setNegative(Neg);
                    calendarData.setPositive(Pos);
                    calendarData.setImg(img);
                    realm.copyToRealmOrUpdate(calendarData);
                }
            }
        });
        return true;
    }

}
