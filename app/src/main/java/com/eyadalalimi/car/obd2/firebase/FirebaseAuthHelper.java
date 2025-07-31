package com.eyadalalimi.car.obd2.firebase;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class FirebaseAuthHelper {

    private static final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onError(String errorMessage);
    }

    public static void loginWithEmail(String email, String password, AuthCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(firebaseAuth.getCurrentUser());
                    } else {
                        callback.onError(task.getException() != null ? task.getException().getMessage() : "Login failed");
                    }
                });
    }

    public static void registerWithEmail(String email, String password, AuthCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(firebaseAuth.getCurrentUser());
                    } else {
                        callback.onError(task.getException() != null ? task.getException().getMessage() : "Registration failed");
                    }
                });
    }

    public static void loginWithGoogle(String idToken, AuthCallback callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(firebaseAuth.getCurrentUser());
                    } else {
                        callback.onError(task.getException() != null ? task.getException().getMessage() : "Google sign-in failed");
                    }
                });
    }

    public static FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public static void logout() {
        firebaseAuth.signOut();
    }
}
