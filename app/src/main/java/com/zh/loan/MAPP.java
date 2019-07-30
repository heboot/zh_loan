package com.zh.loan;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class MAPP extends Application {

    public static MAPP mapp;

    private Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        mapp = this;



        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted( Activity activity) {

            }

            @Override
            public void onActivityResumed( Activity activity) {
                currentActivity = activity;
            }

            @Override
            public void onActivityPaused( Activity activity) {

            }

            @Override
            public void onActivityStopped( Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState( Activity activity,  Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed( Activity activity) {

            }
        });
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }
}
