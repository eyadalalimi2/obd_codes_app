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
import com.proapp.obdcodes.ui.home.HomeActivity;
import com.proapp.obdcodes.viewmodel.AuthViewModel;

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
        hideSystemUI();
        viewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(AuthViewModel.class);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.server_client_id))
                .build();
        googleClient = GoogleSignIn.getClient(this, gso);

        binding.tvLoginTab.setOnClickListener(v -> showLogin());
        binding.tvSignupTab.setOnClickListener(v -> showSignup());

        binding.btnLogin.setOnClickListener(v -> loginWithEmail());
        binding.btnCreateAccount.setOnClickListener(v -> registerWithEmail());
        binding.btnGoogleSignIn.setOnClickListener(v ->
                startActivityForResult(googleClient.getSignInIntent(), RC_SIGN_IN)
        );

        binding.tvForgot.setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class))
        );

        showLogin();

        // مراقبة نتائج تسجيل الدخول (الإيميل/الباسورد)
        viewModel.getLoginResult().observe(this, result -> {
            setLoading(false);
            if (result == null) {
                showToast(R.string.login_failed);
                return;
            }
            if (result.isOk() && result.getToken() != null) {
                saveLogin(result.getToken());
                navigateHome();
            } else {
                showToast(result.getMessage());
            }
        });

        // مراقبة نتائج التسجيل
        viewModel.getRegisterResult().observe(this, result -> {
            setLoading(false);
            if (result == null) {
                showToast(R.string.reg_failed);
                return;
            }
            if (result.isOk() && result.getToken() != null) {
                saveLogin(result.getToken());
                startActivity(new Intent(this, EmailVerificationActivity.class));
                finish();
            } else {
                showToast(result.getMessage());
            }
        });

        // مراقبة نتائج تسجيل دخول Google (يتم تفعيلها فقط بعد استدعاء loginWithGoogle)
        viewModel.getGoogleLoginResult().observe(this, result -> {
            setLoading(false);
            if (result == null) {
                showToast(R.string.login_failed);
                return;
            }
            if (result.isOk() && result.getToken() != null) {
                saveLogin(result.getToken());
                navigateHome();
            } else {
                showToast(result.getMessage());
            }
        });
    }

    private void hideSystemUI() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
            getWindow().getInsetsController().hide(
                    android.view.WindowInsets.Type.statusBars() | android.view.WindowInsets.Type.navigationBars()
            );
            getWindow().getInsetsController().setSystemBarsBehavior(
                    android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            );
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    android.view.View.SYSTEM_UI_FLAG_IMMERSIVE
                            | android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
                            | android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
        }
    }


    private void showLogin() {
        binding.tvAuthTitle.setText(R.string.login_title);
        binding.llLoginForm.setVisibility(View.VISIBLE);
        binding.llSignupForm.setVisibility(View.GONE);
        binding.tvLoginTab.setTextColor(
                ContextCompat.getColor(this, R.color.colorPrimary));
        binding.tvSignupTab.setTextColor(
                ContextCompat.getColor(this, R.color.colorSubText));
    }

    private void showSignup() {
        binding.tvAuthTitle.setText(R.string.signup_title);
        binding.llLoginForm.setVisibility(View.GONE);
        binding.llSignupForm.setVisibility(View.VISIBLE);
        binding.tvLoginTab.setTextColor(
                ContextCompat.getColor(this, R.color.colorSubText));
        binding.tvSignupTab.setTextColor(
                ContextCompat.getColor(this, R.color.colorPrimary));
    }

    private void loginWithEmail() {
        String email = binding.etEmail.getText().toString().trim();
        String pass  = binding.etPassword.getText().toString().trim();
        if (!validate(email, pass, true)) return;

        setLoading(true);
        viewModel.login(email, pass); // يجب أن يقوم بتغيير الـ LiveData في ViewModel
    }

    private void registerWithEmail() {
        String username = binding.etName.getText().toString().trim();
        String email    = binding.etSignupEmail.getText().toString().trim();
        String pass     = binding.etSignupPassword.getText().toString().trim();

        if (username.isEmpty() || !binding.cbAgree.isChecked() ||
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() ||
                pass.length() < 6)
        {
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
        viewModel.register(username, email, pass); // يجب أن يقوم بتغيير الـ LiveData في ViewModel
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
        ApiClient.reset();
    }

    private void navigateHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount acct = task.getResult(ApiException.class);
                if (acct != null) {
                    setLoading(true);
                    viewModel.loginWithGoogle(acct.getIdToken()); // هذا فقط يغير الـ LiveData في ViewModel
                }
            } catch (ApiException e) {
                setLoading(false);
                showToast(R.string.google_signin_failed);
            }
        }
    }
}
