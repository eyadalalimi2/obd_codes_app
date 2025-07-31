package com.eyadalalimi.car.obd2.network;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * عميل Retrofit لإدارة الاتصالات بالشبكة.
 * يقرأ التوكن بأمان من تفضيلات مشفرة ويقلل مستوى السجلات إلى BASIC لحماية البيانات الحساسة.
 */
public class ApiClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://obdcodehub.com/api/";
    public static final String IMAGE_BASE_URL = "https://obdcodehub.com/storage/";

    /** إعادة تهيئة Retrofit عند تحديث التوكن أو تغيير الإعدادات. */
    public static void reset() {
        retrofit = null;
    }

    public static Retrofit getInstance(Context context) {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // استخدم المستوى BASIC لتسجيل الطلبات بطريقة مختصرة وآمنة
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request.Builder builder = original.newBuilder()
                                .header("Accept", "application/json");
                        String token = loadToken(context);
                        if (token != null) {
                            builder.header("Authorization", "Bearer " + token);
                        }
                        return chain.proceed(builder.build());
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService(Context context) {
        return getInstance(context).create(ApiService.class);
    }

    /**
     * تحميل التوكن من EncryptedSharedPreferences إن أمكن، وإلا العودة إلى SharedPreferences الاعتيادية.
     */
    private static String loadToken(Context context) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences securePrefs = EncryptedSharedPreferences.create(
                    "secure_prefs",
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            return securePrefs.getString("auth_token", null);
        } catch (GeneralSecurityException | IOException e) {
            // في حال فشل القراءة من التفضيلات المشفرة، استخدم التفضيلات الافتراضية
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            return prefs.getString("auth_token", null);
        }
    }
}
