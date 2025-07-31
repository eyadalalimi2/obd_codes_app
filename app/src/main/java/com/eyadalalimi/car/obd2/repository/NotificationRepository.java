package com.eyadalalimi.car.obd2.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.eyadalalimi.car.obd2.base.ConnectivityInterceptor;
import com.eyadalalimi.car.obd2.network.ApiClient;
import com.eyadalalimi.car.obd2.network.ApiService;
import com.eyadalalimi.car.obd2.network.model.NotificationResponse;
import com.eyadalalimi.car.obd2.network.model.UserNotification;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository لجلب الإشعارات وإدارة حالتها.
 * تمت إضافة فحص لاستثناء NoConnectivityException رغم أن المعالجة الحالية تقوم فقط بإرجاع null أو false.
 */
public class NotificationRepository {
    private final ApiService api;

    public NotificationRepository(Context ctx) {
        api = ApiClient.getApiService(ctx);
    }

    public LiveData<List<UserNotification>> fetchNotifications() {
        MutableLiveData<List<UserNotification>> data = new MutableLiveData<>();
        api.getNotifications().enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    data.postValue(resp.body().getData());
                } else {
                    data.postValue(null);
                }
            }
            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                if (t instanceof ConnectivityInterceptor.NoConnectivityException) {
                    data.postValue(null);
                } else {
                    data.postValue(null);
                }
            }
        });
        return data;
    }

    public LiveData<Boolean> markRead(long id) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        api.markNotificationRead(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> c, Response<Void> r) {
                result.postValue(r.isSuccessful());
            }
            @Override
            public void onFailure(Call<Void> c, Throwable t) {
                if (t instanceof ConnectivityInterceptor.NoConnectivityException) {
                    result.postValue(false);
                } else {
                    result.postValue(false);
                }
            }
        });
        return result;
    }

    public LiveData<Boolean> delete(long id) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        api.deleteNotification(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> c, Response<Void> r) {
                result.postValue(r.isSuccessful());
            }
            @Override
            public void onFailure(Call<Void> c, Throwable t) {
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
