package com.eyadalalimi.car.obd2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.eyadalalimi.car.obd2.network.ApiClient;
import com.eyadalalimi.car.obd2.network.ApiService;
import com.eyadalalimi.car.obd2.network.model.EncryptionKeyResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KeyManager {
    private static final String PREF_NAME = "app_prefs";
    private static final String KEY_NAME = "db_encryption_key";

    public static void fetchAndStoreKey(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (prefs.contains(KEY_NAME)) return; // المفتاح محفوظ مسبقًا

        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.getEncryptionKey("com.eyadalalimi.car.obd2", "26").enqueue(new Callback<EncryptionKeyResponse>() {
            @Override
            public void onResponse(Call<EncryptionKeyResponse> call, Response<EncryptionKeyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String key = response.body().getKey();
                    prefs.edit().putString(KEY_NAME, key).apply();
                    Log.d("KeyManager", "تم حفظ مفتاح التشفير");
                } else {
                    Log.e("KeyManager", "فشل في الحصول على المفتاح من الخادم");
                }
            }

            @Override
            public void onFailure(Call<EncryptionKeyResponse> call, Throwable t) {
                Log.e("KeyManager", "خطأ في الاتصال: " + t.getMessage());
            }
        });
    }

    public static String getStoredKey(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(KEY_NAME, "defaultKey");
    }
}
