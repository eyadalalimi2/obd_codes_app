package com.proapp.obdcodes.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.databinding.ActivityLoginBinding;
import com.proapp.obdcodes.network.ApiClient;
import com.proapp.obdcodes.ui.home.HomeActivity;
import com.proapp.obdcodes.viewmodel.AuthViewModel;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1001;

    private ActivityLoginBinding binding;
    private AuthViewModel viewModel;
    private GoogleSignInClient googleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ViewModel
        viewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(AuthViewModel.class);

        // Google Sign-In setup
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.server_client_id))
                .build();
        googleClient = GoogleSignIn.getClient(this, gso);

        // event handlers
        binding.btnLogin.setOnClickListener(v -> loginWithEmail());
        binding.btnGoogleSignIn.setOnClickListener(v ->
                startActivityForResult(googleClient.getSignInIntent(), RC_SIGN_IN)
        );
        binding.tvVersion.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    private void loginWithEmail() {
        String email = binding.etEmail.getText().toString().trim();
        String pass  = binding.etPassword.getText().toString().trim();
        if (!validate(email, pass)) return;

        setLoading(true);
        viewModel.login(email, pass).observe(this, resp -> {
            setLoading(false);
            if (resp!=null && resp.getToken()!=null) {
                saveLogin(resp.getToken());
                navigateHome();
            } else {
                showToast(R.string.login_failed);
            }
        });
    }

    private boolean validate(String email, String pass) {
        if (email.isEmpty() || pass.isEmpty()) {
            showToast(R.string.err_fill_all);
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError(getString(R.string.err_invalid_email));
            return false;
        }
        if (pass.length() < 6) {
            binding.etPassword.setError(getString(R.string.err_invalid_pass));
            return false;
        }
        return true;
    }

    private void setLoading(boolean loading) {
        binding.progressAuth.setVisibility(loading? View.VISIBLE: View.GONE);
        binding.btnLogin.setEnabled(!loading);
        binding.btnGoogleSignIn.setEnabled(!loading);
        binding.tvVersion.setEnabled(!loading);
    }

    private void saveLogin(String token) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        prefs.edit()
                .putString("auth_token", token)
                .putBoolean("is_logged_in", true)
                .apply();
        ApiClient.reset();
    }

    private void navigateHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int req, int res, @Nullable Intent data) {
        super.onActivityResult(req, res, data);
        if (req==RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount acct = task.getResult(ApiException.class);
                if (acct!=null) {
                    setLoading(true);
                    viewModel.loginWithGoogle(acct.getIdToken())
                            .observe(this, resp -> {
                                setLoading(false);
                                if (resp!=null && resp.getToken()!=null) {
                                    saveLogin(resp.getToken());
                                    navigateHome();
                                } else {
                                    showToast(R.string.login_failed);
                                }
                            });
                }
            } catch (ApiException e) {
                setLoading(false);
                showToast(R.string.google_signin_failed);
            }
        }
    }
}
