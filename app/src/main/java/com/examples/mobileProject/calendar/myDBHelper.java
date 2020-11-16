package com.examples.mobileProject.calendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class myDBHelper extends SQLiteOpenHelper {
    public Context context;

    //Database information
    static final String DB_NAME = "emotionDB";
    static final int DB_VERSION = 1;

    public myDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    //    최초 DB가 존재하지 않으면 새로 생성한다.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE emotionTBL (dbDate INTEGER PRIMARY KEY, dbPos FLOAT, dbNeg FLOAT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS emotionTBL");
        onCreate(db);
    }
}
