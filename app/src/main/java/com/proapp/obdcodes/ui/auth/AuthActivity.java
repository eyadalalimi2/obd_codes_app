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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.databinding.ActivityAuthBinding;
import com.proapp.obdcodes.network.ApiClient;
import com.proapp.obdcodes.network.model.LoginResponse;
import com.proapp.obdcodes.network.model.RegisterResponse;
import com.proapp.obdcodes.ui.home.HomeActivity;
import com.proapp.obdcodes.viewmodel.AuthViewModel;
import com.proapp.obdcodes.viewmodel.UserViewModel;

public class AuthActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1001;
    private ActivityAuthBinding binding;
    private AuthViewModel viewModel;
    private GoogleSignInClient googleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // نهيئ ViewModel
        viewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(AuthViewModel.class);

        // إعداد Google Sign-In كما كان
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.server_client_id))
                .build();
        googleClient = GoogleSignIn.getClient(this, gso);

        // أزرار التبديل والاستدعاء
        binding.tvLoginTab       .setOnClickListener(v -> showLogin());
        binding.tvSignupTab      .setOnClickListener(v -> showSignup());
        binding.btnLogin         .setOnClickListener(v -> loginWithEmail());
        binding.btnCreateAccount .setOnClickListener(v -> registerWithEmail());
        binding.btnGoogleSignIn  .setOnClickListener(v ->
                startActivityForResult(
                        googleClient.getSignInIntent(), RC_SIGN_IN
                )
        );

        showLogin();
    }

    private void showLogin() {
        binding.tvAuthTitle .setText(R.string.login_title);
        binding.llLoginForm .setVisibility(View.VISIBLE);
        binding.llSignupForm.setVisibility(View.GONE);
        binding.tvLoginTab  .setTextColor(
                ContextCompat.getColor(this, R.color.colorPrimary));
        binding.tvSignupTab .setTextColor(
                ContextCompat.getColor(this, R.color.colorSubText));
    }

    private void showSignup() {
        binding.tvAuthTitle .setText(R.string.signup_title);
        binding.llLoginForm .setVisibility(View.GONE);
        binding.llSignupForm.setVisibility(View.VISIBLE);
        binding.tvLoginTab  .setTextColor(
                ContextCompat.getColor(this, R.color.colorSubText));
        binding.tvSignupTab .setTextColor(
                ContextCompat.getColor(this, R.color.colorPrimary));
    }

    private void loginWithEmail() {
        String email = binding.etEmail.getText().toString().trim();
        String pass  = binding.etPassword.getText().toString().trim();
        if (!validate(email, pass, true)) return;

        setLoading(true);
        viewModel.login(email, pass).observe(this, resp -> {
            setLoading(false);
            if (resp != null && resp.getToken() != null) {
                saveLogin(resp.getToken());
                navigateHome();
            } else {
                showToast(R.string.login_failed);
            }
        });
    }

    private void registerWithEmail() {
        String username = binding.etName.getText().toString().trim();
        String email    = binding.etSignupEmail.getText().toString().trim();
        String pass     = binding.etSignupPassword.getText().toString().trim();
        if (username.isEmpty() || !binding.cbAgree.isChecked() ||
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() ||
                pass.length() < 6) {
            if (username.isEmpty())      showToast(R.string.err_fill_all);
            else if (!binding.cbAgree.isChecked())
                showToast(R.string.err_agree);
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                binding.etSignupEmail.setError(
                        getString(R.string.err_invalid_email));
            else binding.etSignupPassword.setError(
                        getString(R.string.err_invalid_pass));
            return;
        }

        setLoading(true);
        viewModel.register(username, email, pass).observe(this, resp -> {
            setLoading(false);
            if (resp != null && resp.isSuccess() && resp.getToken() != null) {
                saveLogin(resp.getToken());
                navigateHome();
            } else {
                showToast(R.string.reg_failed);
            }
        });
    }

    private boolean validate(String email, String pass, boolean isLogin) {
        if (email.isEmpty() || pass.isEmpty()) {
            showToast(R.string.err_fill_all);
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (isLogin) binding.etEmail.setError(
                    getString(R.string.err_invalid_email));
            return false;
        }
        if (pass.length() < 6) {
            if (isLogin) binding.etPassword.setError(
                    getString(R.string.err_invalid_pass));
            return false;
        }
        return true;
    }

    private void setLoading(boolean loading) {
        binding.progressAuth.setVisibility(
                loading ? View.VISIBLE : View.GONE);
        binding.btnLogin        .setEnabled(!loading);
        binding.btnCreateAccount.setEnabled(!loading);
        binding.btnGoogleSignIn .setEnabled(!loading);
    }

    private void saveLogin(String token) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        prefs.edit()
                .putString("auth_token", token)
                .putBoolean("is_logged_in", true)
                .apply();

        // نعيد بناء الـ Retrofit client ليشمل التوكن الجديد
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
        if (req == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount acct = task.getResult(ApiException.class);
                if (acct != null) {
                    setLoading(true);
                    // تضيف loginWithGoogle بنفس نمط login()
                    viewModel.loginWithGoogle(acct.getIdToken())
                            .observe(this, resp -> {
                                setLoading(false);
                                if (resp != null && resp.getToken() != null) {
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
