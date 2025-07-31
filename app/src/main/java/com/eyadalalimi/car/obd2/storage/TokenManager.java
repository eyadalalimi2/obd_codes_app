// TokenManager.java
package com.eyadalalimi.car.obd2.storage;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * مدير لتخزين واسترجاع رمز المصادقة (token).
 * يستخدم التخزين المشفر عند توفره ويحتفظ بنسخة في التفضيلات العادية لضمان التوافق.
 */
public class TokenManager {

    private static final String PREF_AUTH_TOKEN = "auth_token";
    private static TokenManager instance;

    private final SharedPreferences securePrefs;
    private final SharedPreferences fallbackPrefs;

    private TokenManager(Context context) {
        Context appContext = context.getApplicationContext();
        // التفضيلات العادية للاستخدام الاحتياطي
        this.fallbackPrefs = PreferenceManager.getDefaultSharedPreferences(appContext);
        SharedPreferences tmpSecure;
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            tmpSecure = EncryptedSharedPreferences.create(
                    "secure_prefs",
                    masterKeyAlias,
                    appContext,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            // في حالة فشل إنشاء التفضيلات المشفرة، ستبقى null وسيُستخدم الاحتياطي فقط
            tmpSecure = null;
        }
        this.securePrefs = tmpSecure;
    }

    public static synchronized TokenManager getInstance(Context context) {
        if (instance == null) {
            instance = new TokenManager(context);
        }
        return instance;
    }

    /** حفظ الرمز في التفضيلات المشفرة عند توفرها وكذلك في التفضيلات العادية. */
    public void saveToken(String token) {
        if (securePrefs != null) {
            securePrefs.edit()
                    .putString(PREF_AUTH_TOKEN, token)
                    .apply();
        }
        fallbackPrefs.edit()
                .putString(PREF_AUTH_TOKEN, token)
                .apply();
    }

    /** استرجاع الرمز من التفضيلات المشفرة، وإن لم يتوفر يرجع من التفضيلات العادية. */
    public String getToken() {
        String token = null;
        if (securePrefs != null) {
            token = securePrefs.getString(PREF_AUTH_TOKEN, null);
        }
        if (token == null) {
            token = fallbackPrefs.getString(PREF_AUTH_TOKEN, null);
        }
        return token;
    }

    /** إزالة الرمز من جميع التفضيلات. */
    public void clearToken() {
        if (securePrefs != null) {
            securePrefs.edit()
                    .remove(PREF_AUTH_TOKEN)
                    .apply();
        }
        fallbackPrefs.edit()
                .remove(PREF_AUTH_TOKEN)
                .apply();
    }
}
