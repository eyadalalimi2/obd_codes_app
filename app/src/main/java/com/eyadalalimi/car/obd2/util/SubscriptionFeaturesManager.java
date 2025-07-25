// SubscriptionFeaturesManager.java
package com.eyadalalimi.car.obd2.util;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SubscriptionFeaturesManager {
    private static final String PREFS = "subscription_features";
    private static final String KEY = "features";

    // حفظ الميزات (يفضل الاستدعاء عند كل تحديث للباقة)
    public static void saveFeatures(Context context, List<String> features) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().putStringSet(KEY, new HashSet<>(features)).apply();
    }

    // التحقق من توفر ميزة
    public static boolean hasFeature(Context context, String feature) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        Set<String> features = sp.getStringSet(KEY, null);
        return features != null && features.contains(feature);
    }

    // الحصول على كل الميزات
    public static Set<String> getAllFeatures(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sp.getStringSet(KEY, null);
    }
}
