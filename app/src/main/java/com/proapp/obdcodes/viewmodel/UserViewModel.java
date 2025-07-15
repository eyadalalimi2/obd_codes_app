package com.proapp.obdcodes.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.proapp.obdcodes.network.ApiClient;
import com.proapp.obdcodes.network.ApiService;
import com.proapp.obdcodes.network.model.Subscription;
import com.proapp.obdcodes.network.model.User;
import com.proapp.obdcodes.repository.ProfileRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends AndroidViewModel {

    private final ProfileRepository repo;
    private final ApiService apiService;

    public UserViewModel(@NonNull Application app) {
        super(app);
        repo = new ProfileRepository(app);
        apiService = ApiClient.getInstance(app).create(ApiService.class);
    }

    /**
     * Fetch the authenticated user's profile.
     */
    public LiveData<User> getUserProfile() {
        return repo.getUserProfile();
    }

    /**
     * Update the authenticated user's profile.
     */


    /**
     * Send email verification notification.
     */
    public LiveData<Boolean> sendVerificationEmail() {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        apiService.sendVerificationNotification()
                .enqueue(new Callback<Void>() {
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

    /**
     * Fetch available subscriptions.
     */
    public LiveData<List<Subscription>> getSubscriptions() {
        return repo.getSubscriptions();
    }
    public LiveData<Boolean> logout() {
        return repo.logout();
    }

    /**
     * حذف حساب المستخدم.
     */
    public LiveData<Boolean> deleteAccount() {
        return repo.deleteAccount();
    }
}
