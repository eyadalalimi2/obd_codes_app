package com.eyadalalimi.car.obd2.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.network.ApiClient;
import com.eyadalalimi.car.obd2.ui.home.HomeActivity;
import com.eyadalalimi.car.obd2.viewmodel.AuthViewModel;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnSignIn;
    private TextView tvForgot, tvCreateAccount;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initViewModel();
        bindActions();
    }

    private void initViews() {
        etEmail          = findViewById(R.id.emailField);
        etPassword       = findViewById(R.id.passwordField);
        btnSignIn        = findViewById(R.id.signInButton);
        tvForgot         = findViewById(R.id.forgotPassword);
        tvCreateAccount  = findViewById(R.id.createAccount);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(AuthViewModel.class);

        viewModel.getLoginResult().observe(this, result -> {
            if (result == null) {
                toast(R.string.login_failed);
                btnSignIn.setEnabled(true);
                return;
            }
            if (result.isOk() && result.getToken() != null) {
                saveLogin(result.getToken());
                navigateHome();
            } else {
                toast(result.getMessage());
                btnSignIn.setEnabled(true);
            }
        });
    }

    private void bindActions() {
        btnSignIn.setOnClickListener(v -> attemptLogin());
        tvForgot.setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class))
        );
        tvCreateAccount.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String pass  = etPassword.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            toast(R.string.err_fill_all);
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.err_invalid_email));
            return;
        }
        if (pass.length() < 6) {
            etPassword.setError(getString(R.string.err_invalid_pass));
            return;
        }

        btnSignIn.setEnabled(false);
        viewModel.login(email, pass);
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

    private void toast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }
    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
