package com.zh.loan;

import android.app.Application;

public class MAPP extends Application {

    public static MAPP mapp;

    @Override
    public void onCreate() {
        super.onCreate();

        mapp = this;
    }
}
