package com.proapp.obdcodes.ui.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.auth.AuthActivity;
import com.proapp.obdcodes.ui.home.HomeActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // تنفيذ بعد تأخير قصير لإظهار الشاشة
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
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
