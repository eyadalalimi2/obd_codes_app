package com.proapp.obdcodes.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.proapp.obdcodes.network.ApiClient;
import com.proapp.obdcodes.network.ApiService;
import com.proapp.obdcodes.network.model.MessageResponse;
import com.proapp.obdcodes.network.model.Subscription;
import com.proapp.obdcodes.network.model.UpdateProfileRequest;
import com.proapp.obdcodes.network.model.User;
import com.proapp.obdcodes.network.model.UserProfileResponse;
import com.proapp.obdcodes.repository.ProfileRepository;

import java.util.List;

import okhttp3.MultipartBody;
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
    public LiveData<Boolean> sendEmailVerification() {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        apiService.sendEmailVerification()
                .enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        // true إذا نجح (status 200) وجسم الرد non-null
                        result.postValue(response.isSuccessful() && response.body() != null);
                    }
                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {
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

    public LiveData<User> updateProfileData(UpdateProfileRequest request) {
        MutableLiveData<User> result = new MutableLiveData<>();
        apiService.updateProfileData(request)
                .enqueue(new Callback<UserProfileResponse>() {
                    @Override
                    public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            result.postValue(response.body().getUser());
                        } else {
                            result.postValue(null);
                        }
                    }
                    @Override
                    public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                        result.postValue(null);
                    }
                });
        return result;
    }

    public LiveData<User> updateProfileAvatar(MultipartBody.Part imagePart) {
        MutableLiveData<User> result = new MutableLiveData<>();
        apiService.updateProfileAvatar(imagePart)
                .enqueue(new Callback<UserProfileResponse>() {
                    @Override
                    public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            result.postValue(response.body().getUser());
                        } else {
                            result.postValue(null);
                        }
                    }
                    @Override
                    public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                        result.postValue(null);
                    }
                });
        return result;
    }

}
