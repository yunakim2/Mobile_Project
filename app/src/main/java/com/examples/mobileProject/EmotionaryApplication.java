package com.examples.mobileProject;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class EmotionaryApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("emotionary.realm")
                .allowWritesOnUiThread(true)
                .allowQueriesOnUiThread(true)
                .build();
        Realm.setDefaultConfiguration(config);

    }
}
