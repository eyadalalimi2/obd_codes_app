package com.eyadalalimi.car.obd2.network;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static String currentToken = null;
    private static final String BASE_URL = "https://obdcode.xyz/api/";
    public static final String IMAGE_BASE_URL = "https://obdcode.xyz/storage/";

    // لا نخزن الـ Retrofit نهائيًا ـ نجعلها تتجدد
    private static Retrofit retrofit;

    /** إذا أردنا إجبار إعادة بناء client (بعد حفظ توكن جديد) */
    public static void reset() {
        retrofit = null;
    }

    public static Retrofit getInstance(Context context) {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        SharedPreferences prefs =
                                PreferenceManager.getDefaultSharedPreferences(context);
                        String token = prefs.getString("auth_token", null);

                        Request.Builder builder = original.newBuilder()
                                .header("Accept", "application/json");
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
    public static void resetWithToken(String token) {
        currentToken = token;

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder()
                            .header("Accept", "application/json");

                    // إذا وُجد توكن نضيفه
                    if (currentToken != null) {
                        builder.header("Authorization", "Bearer " + currentToken);
                    }

                    Request request = builder.build();
                    return chain.proceed(request);
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static Retrofit getClient() {
        if (retrofit == null) {
            resetWithToken(null); // بدون توكن
        }
        return retrofit;
    }


}
