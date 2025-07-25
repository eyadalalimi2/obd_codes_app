package com.eyadalalimi.car.obd2.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.eyadalalimi.car.obd2.network.ApiClient;
import com.eyadalalimi.car.obd2.network.ApiService;
import com.eyadalalimi.car.obd2.network.model.Subscription;
import com.eyadalalimi.car.obd2.network.model.SubscriptionRequest;

import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscriptionRepository {

    private final ApiService api;
    private final Context context;
    private static final String PREF_NAME = "subscription_prefs";
    private static final String KEY_FEATURES = "features_set";

    public SubscriptionRepository(@NonNull Context context) {
        this.api = ApiClient.getInstance(context).create(ApiService.class);
        this.context = context.getApplicationContext();
    }

    public interface CurrentSubscriptionCallback {
        void onSuccess(@NonNull Subscription subscription);
        void onFailure(@NonNull String errorMessage);
    }

    public interface SubscriptionCallback {
        void onSuccess();
        void onFailure(@NonNull String errorMessage);
    }

    // جلب الاشتراك وتخزين الميزات مع لوج
    public void getCurrentSubscription(
            @NonNull CurrentSubscriptionCallback callback
    ) {
        api.getCurrentSubscriptionStatus()
                .enqueue(new Callback<Subscription>() {
                    @Override
                    public void onResponse(
                            Call<Subscription> call,
                            Response<Subscription> response
                    ) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                Log.d("SubscriptionRepo", "Received features: " + response.body().getFeatures());
                                storeFeaturesLocally(response.body().getFeatures());
                                callback.onSuccess(response.body());
                            } else {
                                callback.onFailure("لم يتم تحميل بيانات الاشتراك");
                            }
                        } else {
                            callback.onFailure("Response error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Subscription> call, Throwable t) {
                        callback.onFailure("فشل في الاتصال: " + t.getMessage());
                    }
                });
    }

    private void storeFeaturesLocally(java.util.List<String> features) {
        if (features == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> set = new HashSet<>(features);
        prefs.edit().putStringSet(KEY_FEATURES, set).apply();
    }

    public Set<String> getStoredFeatures() {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getStringSet(KEY_FEATURES, new HashSet<>());
    }

    public boolean hasFeature(String featureKey) {
        return getStoredFeatures().contains(featureKey);
    }

    public void renewSubscription(
            @NonNull SubscriptionRequest request,
            @NonNull SubscriptionCallback callback
    ) {
        api.renewSubscription(request)
                .enqueue(new Callback<Subscription>() {
                    @Override
                    public void onResponse(
                            Call<Subscription> call,
                            Response<Subscription> response
                    ) {
                        if (response.isSuccessful() && response.body() != null) {
                            storeFeaturesLocally(response.body().getFeatures());
                            callback.onSuccess();
                        } else {
                            callback.onFailure("فشل في التجديد: " + response.message());
                        }
                    }
                    @Override
                    public void onFailure(Call<Subscription> call, Throwable t) {
                        callback.onFailure("فشل في الاتصال: " + t.getMessage());
                    }
                });
    }

    public void cancelSubscription(
            @NonNull SubscriptionRequest request,
            @NonNull SubscriptionCallback callback
    ) {
        api.cancelSubscription(request)
                .enqueue(new Callback<Subscription>() {
                    @Override
                    public void onResponse(
                            Call<Subscription> call,
                            Response<Subscription> response
                    ) {
                        if (response.isSuccessful()) {
                            storeFeaturesLocally(new java.util.ArrayList<>());
                            callback.onSuccess();
                        } else {
                            callback.onFailure("فشل في الإلغاء: " + response.message());
                        }
                    }
                    @Override
                    public void onFailure(Call<Subscription> call, Throwable t) {
                        callback.onFailure("فشل في الاتصال: " + t.getMessage());
                    }
                });
    }

    public void createSubscription(
            @NonNull SubscriptionRequest request,
            @NonNull SubscriptionCallback callback
    ) {
        api.subscribeToPlan(request)
                .enqueue(new Callback<Subscription>() {
                    @Override
                    public void onResponse(
                            Call<Subscription> call,
                            Response<Subscription> response
                    ) {
                        if (response.isSuccessful() && response.body() != null) {
                            storeFeaturesLocally(response.body().getFeatures());
                            callback.onSuccess();
                        } else {
                            callback.onFailure("فشل في الاشتراك: " + response.message());
                        }
                    }
                    @Override
                    public void onFailure(Call<Subscription> call, Throwable t) {
                        callback.onFailure("فشل في الاتصال: " + t.getMessage());
                    }
                });
    }
}
