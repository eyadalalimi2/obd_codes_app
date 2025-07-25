package com.eyadalalimi.car.obd2;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.onesignal.OneSignal;
import com.eyadalalimi.car.obd2.R;

public class App extends Application {
    public static SharedPreferences prefs;

    @Override
    public void onCreate() {
        super.onCreate();

        // SharedPreferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // OneSignal
        OneSignal.initWithContext(this);
        OneSignal.setAppId(getString(R.string.todo));

        // Firebase
        FirebaseAnalytics.getInstance(this);
    }
}
