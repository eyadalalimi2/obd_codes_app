package com.proapp.obdcodes.ui.subscription;

import android.content.Context;

import androidx.annotation.NonNull;

import com.proapp.obdcodes.network.ApiClient;
import com.proapp.obdcodes.network.ApiService;
import com.proapp.obdcodes.network.model.Subscription;
import com.proapp.obdcodes.network.model.SubscriptionRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscriptionRepository {

    private final ApiService api;

    public SubscriptionRepository(@NonNull Context context) {
        this.api = ApiClient
                .getInstance(context)
                .create(ApiService.class);
    }

    // --- واجهات الكولباك للنتائج ---
    public interface CurrentSubscriptionCallback {
        void onSuccess(@NonNull Subscription subscription);
        void onFailure(@NonNull String errorMessage);
    }

    public interface SubscriptionCallback {
        void onSuccess();
        void onFailure(@NonNull String errorMessage);
    }

    // --- جلب الاشتراك الحالي ---
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
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onFailure(
                                    "خطأ في جلب حالة الاشتراك: "
                                            + response.message()
                            );
                        }
                    }
                    @Override
                    public void onFailure(Call<Subscription> call, Throwable t) {
                        callback.onFailure(
                                "فشل في الاتصال: " + t.getMessage()
                        );
                    }
                });
    }

    // --- تجديد الاشتراك ---
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
                        if (response.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure(
                                    "فشل في التجديد: " + response.message()
                            );
                        }
                    }
                    @Override
                    public void onFailure(Call<Subscription> call, Throwable t) {
                        callback.onFailure(
                                "فشل في الاتصال: " + t.getMessage()
                        );
                    }
                });
    }

    // --- إلغاء الاشتراك ---
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
                            callback.onSuccess();
                        } else {
                            callback.onFailure(
                                    "فشل في الإلغاء: " + response.message()
                            );
                        }
                    }
                    @Override
                    public void onFailure(Call<Subscription> call, Throwable t) {
                        callback.onFailure(
                                "فشل في الاتصال: " + t.getMessage()
                        );
                    }
                });
    }

    // --- الاشتراك الجديد (إن دعت الحاجة) ---
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
                        if (response.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure(
                                    "فشل في الاشتراك: " + response.message()
                            );
                        }
                    }
                    @Override
                    public void onFailure(Call<Subscription> call, Throwable t) {
                        callback.onFailure(
                                "فشل في الاتصال: " + t.getMessage()
                        );
                    }
                });
    }
}
