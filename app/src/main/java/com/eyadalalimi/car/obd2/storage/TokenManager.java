// TokenManager.java
package com.eyadalalimi.car.obd2.storage;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public class TokenManager {

    private static final String PREF_AUTH_TOKEN = "auth_token";
    private static TokenManager instance;
    private final SharedPreferences prefs;

    private TokenManager(Context context) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public static synchronized TokenManager getInstance(Context context) {
        if (instance == null) {
            instance = new TokenManager(context);
        }
        return instance;
    }

    public void saveToken(String token) {
        prefs.edit()
                .putString(PREF_AUTH_TOKEN, token)
                .apply();
    }

    public String getToken() {
        return prefs.getString(PREF_AUTH_TOKEN, null);
    }

    public void clearToken() {
        prefs.edit()
                .remove(PREF_AUTH_TOKEN)
                .apply();
    }
}
