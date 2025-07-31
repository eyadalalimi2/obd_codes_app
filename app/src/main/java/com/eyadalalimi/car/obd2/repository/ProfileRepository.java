package com.eyadalalimi.car.obd2.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eyadalalimi.car.obd2.base.ConnectivityInterceptor;
import com.eyadalalimi.car.obd2.network.model.Subscription;
import com.eyadalalimi.car.obd2.network.ApiClient;
import com.eyadalalimi.car.obd2.network.ApiService;
import com.eyadalalimi.car.obd2.network.model.User;
import com.eyadalalimi.car.obd2.network.model.UserProfileResponse;

import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository لإدارة الملف الشخصي والاشتراك للمستخدم.
 * تمت إضافة فحص لاستثناء NoConnectivityException رغم أن المعالجة الحالية تعيد null أو قائمة فارغة.
 */
public class ProfileRepository {

    private final ApiService api;

    public ProfileRepository(Context context) {
        this.api = ApiClient
                .getInstance(context)
                .create(ApiService.class);
    }

    /**
     * Fetches the currently authenticated user's profile.
     */
    public LiveData<User> getUserProfile() {
        MutableLiveData<User> live = new MutableLiveData<>();
        api.getUserProfile().enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    live.setValue(resp.body().getUser());
                } else {
                    live.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                if (t instanceof ConnectivityInterceptor.NoConnectivityException) {
                    live.setValue(null);
                } else {
                    live.setValue(null);
                }
            }
        });
        return live;
    }

    /**
     * Fetches the list of available subscriptions.
     */
    public LiveData<List<Subscription>> getSubscriptions() {
        MutableLiveData<List<Subscription>> live = new MutableLiveData<>();
        api.getCurrentSubscriptionStatus().enqueue(new Callback<Subscription>() {
            @Override
            public void onResponse(Call<Subscription> call, Response<Subscription> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Subscription> list = Collections.singletonList(response.body());
                    live.setValue(list);
                } else {
                    live.setValue(Collections.emptyList());
                }
            }

            @Override
            public void onFailure(Call<Subscription> call, Throwable t) {
                if (t instanceof ConnectivityInterceptor.NoConnectivityException) {
                    live.setValue(Collections.emptyList());
                } else {
                    live.setValue(Collections.emptyList());
                }
            }
        });
        return live;
    }

    public LiveData<Boolean> logout() {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        api.logout().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                result.postValue(response.isSuccessful());
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (t instanceof ConnectivityInterceptor.NoConnectivityException) {
                    result.postValue(false);
                } else {
                    result.postValue(false);
                }
            }
        });
        return result;
    }

    public LiveData<Boolean> deleteAccount() {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        api.deleteAccount().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                result.postValue(response.isSuccessful());
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (t instanceof ConnectivityInterceptor.NoConnectivityException) {
                    result.postValue(false);
                } else {
                    result.postValue(false);
                }
            }
        });
        return result;
    }
}
