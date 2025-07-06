package com.proapp.obdcodes.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.proapp.obdcodes.network.ApiClient;
import com.proapp.obdcodes.network.ApiService;
import com.proapp.obdcodes.network.model.GoogleLoginRequest;
import com.proapp.obdcodes.network.model.LoginRequest;
import com.proapp.obdcodes.network.model.LoginResponse;
import com.proapp.obdcodes.network.model.RegisterRequest;
import com.proapp.obdcodes.network.model.RegisterResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private final ApiService api;

    public AuthRepository(Application app) {
        api = ApiClient.getInstance(app).create(ApiService.class);
    }

    public LiveData<LoginResponse> login(String email, String password) {
        MutableLiveData<LoginResponse> data = new MutableLiveData<>();
        api.login(new LoginRequest(email, password))
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            data.postValue(resp.body());
                        } else {
                            // هنا نطبع كود الخطأ وجسمه
                            try {
                                Log.e("AuthRepo", "Login failed: HTTP " + resp.code()
                                        + " — " + resp.errorBody().string());
                            } catch (IOException e) {
                                Log.e("AuthRepo", "Error reading errorBody", e);
                            }
                            data.postValue(null);
                        }
                    }
                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.e("AuthRepo", "Network failure", t);
                        data.postValue(null);
                    }
                });
        return data;
    }


    public LiveData<RegisterResponse> register(String username, String email, String password) {
        MutableLiveData<RegisterResponse> data = new MutableLiveData<>();
        api.register(new RegisterRequest(username, email, password))
                .enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> resp) {
                        data.postValue(resp.isSuccessful() ? resp.body() : null);
                    }
                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        data.postValue(null);
                    }
                });
        return data;
    }

    public LiveData<LoginResponse> loginWithGoogle(String idToken) {
        MutableLiveData<LoginResponse> data = new MutableLiveData<>();
        api.loginWithGoogle(new GoogleLoginRequest(idToken))
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> resp) {
                        data.postValue(resp.isSuccessful() ? resp.body() : null);
                    }
                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        data.postValue(null);
                    }
                });
        return data;
    }
}
