package com.proapp.obdcodes.ui.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.android.datatransport.backend.cct.BuildConfig;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.ApiClient;
import com.proapp.obdcodes.network.ApiService;
import com.proapp.obdcodes.network.model.EncryptionKeyResponse;
import com.proapp.obdcodes.ui.auth.AuthActivity;
import com.proapp.obdcodes.ui.home.HomeActivity;
import com.proapp.obdcodes.ui.onboarding.OnboardingActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 1500;
    private static final String ENCRYPTION_KEY_PREF = "db_encryption_key";
    private final String PACKAGE_NAME = getPackageName();
    private final String VERSION_CODE = BuildConfig.VERSION_NAME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        fetchEncryptionKey();
    }

    private void fetchEncryptionKey() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String existingKey = prefs.getString(ENCRYPTION_KEY_PREF, null);

        if (existingKey != null) {
            continueToApp(); // المفتاح موجود مسبقًا
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getEncryptionKey(PACKAGE_NAME, VERSION_CODE).enqueue(new Callback<EncryptionKeyResponse>() {
            @Override
            public void onResponse(Call<EncryptionKeyResponse> call, Response<EncryptionKeyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String key = response.body().getKey();
                    prefs.edit().putString(ENCRYPTION_KEY_PREF, key).apply();
                    Log.d("Splash", "تم استلام وحفظ مفتاح التشفير.");
                } else {
                    Log.e("Splash", "فشل في الحصول على المفتاح. سيتم استخدام مفتاح افتراضي.");
                    prefs.edit().putString(ENCRYPTION_KEY_PREF, "defaultKey").apply();
                }
                continueToApp();
            }

            @Override
            public void onFailure(Call<EncryptionKeyResponse> call, Throwable t) {
                Log.e("Splash", "فشل الاتصال بالخادم: " + t.getMessage());
                prefs.edit().putString(ENCRYPTION_KEY_PREF, "defaultKey").apply();
                continueToApp();
            }
        });
    }

    private void continueToApp() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            boolean isFirstLaunch = prefs.getBoolean("is_first_launch", true);

            if (isFirstLaunch) {
                prefs.edit().putBoolean("is_first_launch", false).apply();
                navigateTo(OnboardingActivity.class);
                return;
            }

            if (isUserLoggedIn()) {
                navigateTo(HomeActivity.class);
            } else {
                navigateTo(AuthActivity.class);
            }
        }, SPLASH_DELAY);
    }


    private boolean isUserLoggedIn() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String token = prefs.getString("auth_token", null);
        boolean loggedIn = prefs.getBoolean("is_logged_in", false);
        return loggedIn && token != null && !token.trim().isEmpty();
    }

    private void navigateTo(Class<?> target) {
        startActivity(new Intent(this, target));
        finish();
    }
}
