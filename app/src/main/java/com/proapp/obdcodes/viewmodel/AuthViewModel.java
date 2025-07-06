package com.proapp.obdcodes.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.proapp.obdcodes.network.model.LoginResponse;
import com.proapp.obdcodes.network.model.RegisterResponse;
import com.proapp.obdcodes.repository.AuthRepository;

public class AuthViewModel extends AndroidViewModel {
    private final AuthRepository repo;

    public AuthViewModel(@NonNull Application app) {
        super(app);
        repo = new AuthRepository(app);
    }

    public LiveData<LoginResponse> login(String email, String password) {
        return repo.login(email, password);
    }

    public LiveData<RegisterResponse> register(String username, String email, String password) {
        return repo.register(username, email, password);
    }

    public LiveData<LoginResponse> loginWithGoogle(String idToken) {
        return repo.loginWithGoogle(idToken);
    }
}
