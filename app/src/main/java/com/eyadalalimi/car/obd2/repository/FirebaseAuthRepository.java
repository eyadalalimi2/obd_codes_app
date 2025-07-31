package com.eyadalalimi.car.obd2.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthRepository {

    private final FirebaseAuth firebaseAuth;
    private final MutableLiveData<FirebaseUser> userLiveData;
    private final MutableLiveData<Boolean> loggedOutLiveData;

    public FirebaseAuthRepository(Application application) {
        firebaseAuth = FirebaseAuth.getInstance();
        userLiveData = new MutableLiveData<>();
        loggedOutLiveData = new MutableLiveData<>();

        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.postValue(firebaseAuth.getCurrentUser());
            loggedOutLiveData.postValue(false);
        }
    }

    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userLiveData.postValue(firebaseAuth.getCurrentUser());
                        loggedOutLiveData.postValue(false);
                    } else {
                        userLiveData.postValue(null);
                    }
                });
    }

    public void register(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userLiveData.postValue(firebaseAuth.getCurrentUser());
                        loggedOutLiveData.postValue(false);
                    } else {
                        userLiveData.postValue(null);
                    }
                });
    }

    public void logout() {
        firebaseAuth.signOut();
        loggedOutLiveData.postValue(true);
    }

    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
}
