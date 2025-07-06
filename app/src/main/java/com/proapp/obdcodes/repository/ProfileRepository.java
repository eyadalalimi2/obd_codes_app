package com.proapp.obdcodes.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.proapp.obdcodes.network.model.Subscription;
import com.proapp.obdcodes.network.ApiClient;
import com.proapp.obdcodes.network.ApiService;
import com.proapp.obdcodes.network.model.UpdateProfileRequest;
import com.proapp.obdcodes.network.model.User;
import com.proapp.obdcodes.network.model.UserProfileResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                live.setValue(null);
            }
        });
        return live;
    }

    /**
     * Sends an update request for the authenticated user's profile.
     */
    public LiveData<User> updateUserProfile(UpdateProfileRequest req) {
        MutableLiveData<User> live = new MutableLiveData<>();
        api.updateUserProfile(req).enqueue(new Callback<UserProfileResponse>() {
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
                live.setValue(null);
            }
        });
        return live;
    }

    /**
     * Fetches the list of available subscriptions.
     */
    public LiveData<List<Subscription>> getSubscriptions() {
        MutableLiveData<List<Subscription>> live = new MutableLiveData<>();
        api.getSubscriptions().enqueue(new Callback<List<Subscription>>() {
            @Override
            public void onResponse(Call<List<Subscription>> call,
                                   Response<List<Subscription>> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    live.setValue(resp.body());
                } else {
                    live.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Subscription>> call, Throwable t) {
                live.setValue(null);
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
                result.postValue(false);
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
                result.postValue(false);
            }
        });
        return result;
    }
}
