// com.proapp.obdcodes.ui.plans/PlanRepository.java
package com.eyadalalimi.car.obd2.repository;

import android.content.Context;
import androidx.annotation.NonNull;
import com.eyadalalimi.car.obd2.base.ConnectivityInterceptor;
import com.eyadalalimi.car.obd2.network.ApiClient;
import com.eyadalalimi.car.obd2.network.ApiService;
import com.eyadalalimi.car.obd2.network.model.Plan;
import com.eyadalalimi.car.obd2.network.model.SubscriptionRequest;
import com.eyadalalimi.car.obd2.network.model.ActivationRequest;
import com.eyadalalimi.car.obd2.network.model.Subscription;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanRepository {

    private final ApiService api;

    public PlanRepository(@NonNull Context ctx) {
        this.api = ApiClient
                .getInstance(ctx)
                .create(ApiService.class);
    }

    public interface PlansCallback {
        void onSuccess(@NonNull List<Plan> plans);
        void onFailure(@NonNull String errorMessage);
    }

    public interface SubscriptionCallback {
        void onSuccess();
        void onFailure(@NonNull String errorMessage);
    }

    public interface ActivateCallback {
        void onSuccess();
        void onFailure(@NonNull String errorMessage);
    }

    public void getAllPlans(@NonNull final PlansCallback cb) {
        api.getAllPlans().enqueue(new Callback<List<Plan>>() {
            @Override
            public void onResponse(Call<List<Plan>> call, Response<List<Plan>> r) {
                if (r.isSuccessful() && r.body() != null) cb.onSuccess(r.body());
                else cb.onFailure("خطأ في تحميل الباقات: " + r.message());
            }
            @Override
            public void onFailure(Call<List<Plan>> call, Throwable t) {
                if (t instanceof ConnectivityInterceptor.NoConnectivityException) {
                    cb.onFailure("لا يوجد اتصال بالإنترنت");
                } else {
                    cb.onFailure("فشل في الاتصال: " + t.getMessage());
                }
            }
        });
    }

    public void subscribeToPlan(
            @NonNull SubscriptionRequest req,
            @NonNull final SubscriptionCallback cb
    ) {
        api.subscribeToPlan(req).enqueue(new Callback<Subscription>() {
            @Override
            public void onResponse(Call<Subscription> c, Response<Subscription> r) {
                if (r.isSuccessful()) cb.onSuccess();
                else cb.onFailure("خطأ في الاشتراك: " + r.message());
            }
            @Override
            public void onFailure(Call<Subscription> c, Throwable t) {
                if (t instanceof ConnectivityInterceptor.NoConnectivityException) {
                    cb.onFailure("لا يوجد اتصال بالإنترنت");
                } else {
                    cb.onFailure("فشل في الاتصال: " + t.getMessage());
                }
            }
        });
    }

    public void activateByCode(
            @NonNull ActivationRequest req,
            @NonNull final ActivateCallback cb
    ) {
        api.activatePlanByCode(req).enqueue(new Callback<Subscription>() {
            @Override
            public void onResponse(Call<Subscription> c, Response<Subscription> r) {
                if (r.isSuccessful()) cb.onSuccess();
                else cb.onFailure("كود غير صالح أو منتهي: " + r.message());
            }
            @Override
            public void onFailure(Call<Subscription> c, Throwable t) {
                if (t instanceof ConnectivityInterceptor.NoConnectivityException) {
                    cb.onFailure("لا يوجد اتصال بالإنترنت");
                } else {
                    cb.onFailure("فشل في الاتصال: " + t.getMessage());
                }
            }
        });
    }
}
