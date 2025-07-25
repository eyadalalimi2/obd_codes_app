package com.eyadalalimi.car.obd2.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eyadalalimi.car.obd2.network.ApiClient;
import com.eyadalalimi.car.obd2.network.ApiService;
import com.eyadalalimi.car.obd2.network.model.GoogleLoginRequest;
import com.eyadalalimi.car.obd2.network.model.LoginRequest;
import com.eyadalalimi.car.obd2.network.model.LoginResponse;
import com.eyadalalimi.car.obd2.network.model.RegisterRequest;
import com.eyadalalimi.car.obd2.network.model.RegisterResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private final ApiService api;

    public AuthRepository(Application app) {
        api = ApiClient.getInstance(app).create(ApiService.class);
    }

    public static class Result {
        private final boolean ok;
        private final String message;
        private final String token;

        public Result(boolean ok, String message, String token) {
            this.ok = ok;
            this.message = message;
            this.token = token;
        }

        public boolean isOk() { return ok; }
        public String getMessage() { return message; }
        public String getToken() { return token; }
    }

    public LiveData<Result> login(String email, String password) {
        MutableLiveData<Result> data = new MutableLiveData<>();
        api.login(new LoginRequest(email, password))
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> resp) {
                        if (resp.isSuccessful() && resp.body() != null && resp.body().getToken() != null) {
                            data.postValue(new Result(true, "تم تسجيل الدخول بنجاح", resp.body().getToken()));
                        } else {
                            String msg = "فشل تسجيل الدخول";
                            if (resp.errorBody() != null) {
                                try {
                                    String error = resp.errorBody().string();
                                    if (error.contains("message")) {
                                        int start = error.indexOf(":\"") + 2;
                                        int end = error.indexOf("\"", start);
                                        msg = error.substring(start, end);
                                    } else {
                                        msg = error;
                                    }
                                } catch (IOException ignored) {}
                            }
                            data.postValue(new Result(false, msg, null));
                        }
                    }
                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        data.postValue(new Result(false, "تعذر الاتصال بالخادم", null));
                    }
                });
        return data;
    }


    public LiveData<Result> register(String username, String email, String password) {
        MutableLiveData<Result> data = new MutableLiveData<>();
        api.register(new RegisterRequest(username, email, password))
                .enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> resp) {
                        if (resp.isSuccessful() && resp.body() != null && resp.body().getToken() != null) {
                            data.postValue(new Result(true, "تم إنشاء الحساب بنجاح", resp.body().getToken()));
                        } else {
                            String msg = "فشل إنشاء الحساب";
                            if (resp.errorBody() != null) {
                                try { msg = resp.errorBody().string(); } catch (IOException ignored) {}
                            }
                            data.postValue(new Result(false, msg, null));
                        }
                    }
                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        data.postValue(new Result(false, "فشل الاتصال: " + t.getMessage(), null));
                    }
                });
        return data;
    }

    public LiveData<Result> loginWithGoogle(String idToken) {
        MutableLiveData<Result> data = new MutableLiveData<>();
        api.loginWithGoogle(new GoogleLoginRequest(idToken))
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> resp) {
                        if (resp.isSuccessful() && resp.body() != null && resp.body().getToken() != null) {
                            data.postValue(new Result(true, "تم تسجيل الدخول عبر Google", resp.body().getToken()));
                        } else {
                            String msg = "فشل تسجيل الدخول عبر Google";
                            if (resp.errorBody() != null) {
                                try { msg = resp.errorBody().string(); } catch (IOException ignored) {}
                            }
                            data.postValue(new Result(false, msg, null));
                        }
                    }
                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        data.postValue(new Result(false, "فشل الاتصال: " + t.getMessage(), null));
                    }
                });
        return data;
    }
}
