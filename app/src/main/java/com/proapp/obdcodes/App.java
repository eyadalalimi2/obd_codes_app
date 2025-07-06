package com.proapp.obdcodes;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.onesignal.OneSignal;

public class App extends Application {
    public static SharedPreferences prefs;

    @Override
    public void onCreate() {
        super.onCreate();

        // SharedPreferences بدلاً من DataStore
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // تهيئة OneSignal
        OneSignal.initWithContext(this);
        OneSignal.setAppId(getString(R.string.todo));

        // تهيئة Firebase
        FirebaseAnalytics.getInstance(this);
    }
}
