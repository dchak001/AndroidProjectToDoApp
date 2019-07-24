package com.example.mytodoapp;

import android.app.Application;

import io.realm.Realm;

public class RealmAssignmentApplication  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

}