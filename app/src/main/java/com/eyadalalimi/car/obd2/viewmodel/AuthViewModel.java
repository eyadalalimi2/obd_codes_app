package com.eyadalalimi.car.obd2.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eyadalalimi.car.obd2.repository.AuthRepository;

public class AuthViewModel extends AndroidViewModel {
    private final AuthRepository repo;

    private final MutableLiveData<AuthRepository.Result> loginResult = new MutableLiveData<>();
    private final MutableLiveData<AuthRepository.Result> registerResult = new MutableLiveData<>();
    private final MutableLiveData<AuthRepository.Result> googleLoginResult = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application app) {
        super(app);
        repo = new AuthRepository(app);
    }

    public LiveData<AuthRepository.Result> getLoginResult() { return loginResult; }
    public LiveData<AuthRepository.Result> getRegisterResult() { return registerResult; }
    public LiveData<AuthRepository.Result> getGoogleLoginResult() { return googleLoginResult; }

    // عند الاستدعاء يتم تحديث الـ LiveData داخلياً ليتم المراقبة من الـ Activity مباشرة
    public void login(String email, String password) {
        repo.login(email, password).observeForever(loginResult::postValue);
    }

    public void register(String username, String email, String password) {
        repo.register(username, email, password).observeForever(registerResult::postValue);
    }

    public void loginWithGoogle(String idToken) {
        repo.loginWithGoogle(idToken).observeForever(googleLoginResult::postValue);
    }
}

