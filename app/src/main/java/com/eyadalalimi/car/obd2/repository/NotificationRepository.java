package com.eyadalalimi.car.obd2.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.eyadalalimi.car.obd2.network.ApiClient;
import com.eyadalalimi.car.obd2.network.ApiService;
import com.eyadalalimi.car.obd2.network.model.NotificationResponse;
import com.eyadalalimi.car.obd2.network.model.UserNotification;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                data.postValue(null);
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
                result.postValue(false);
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
                result.postValue(false);
            }
        });
        return result;
    }
}
