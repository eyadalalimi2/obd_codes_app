package com.eyadalalimi.car.obd2.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashSet;
import java.util.Set;

public class SubscriptionManager {

    private static final String PREFS_NAME = "subscription_prefs";
    private static final String KEY_FEATURES = "features";

    // حفظ قائمة الميزات (عند جلبها من السيرفر)
    public static void saveFeatures(Context context, JSONArray features) {
        Set<String> featureSet = new HashSet<>();
        for (int i = 0; i < features.length(); i++) {
            featureSet.add(features.optString(i));
        }
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putStringSet(KEY_FEATURES, featureSet).apply();
    }

    // قراءة قائمة الميزات
    public static Set<String> getFeatures(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getStringSet(KEY_FEATURES, new HashSet<>());
    }

    // التحقق: هل الميزة مفعلة؟
    public static boolean hasFeature(Context context, String featureKey) {
        return getFeatures(context).contains(featureKey);
    }

    // حذف البيانات (مثلاً عند تسجيل خروج)
    public static void clear(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
}
