package com.eyadalalimi.car.obd2.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.viewmodel.AuthViewModel;

public class RegisterActivity extends AppCompatActivity {
    private EditText etEmail, etPassword, etConfirm;
    private Button btnSignUp;
    private TextView tvAlready;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initViewModel();
        bindActions();
    }

    private void initViews() {
        etEmail    = findViewById(R.id.emailField);
        etPassword = findViewById(R.id.passwordField);
        etConfirm  = findViewById(R.id.confirmPasswordField);
        btnSignUp  = findViewById(R.id.signUpButton);
        tvAlready  = findViewById(R.id.alreadyAccount);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(AuthViewModel.class);

        viewModel.getRegisterResult().observe(this, result -> {
            if (result == null) {
                toast(R.string.reg_failed);
                btnSignUp.setEnabled(true);
                return;
            }
            if (result.isOk() && result.getToken() != null) {
                startActivity(new Intent(this, EmailVerificationActivity.class));
                finish();
            } else {
                toast(result.getMessage());
                btnSignUp.setEnabled(true);
            }
        });
    }

    private void bindActions() {
        btnSignUp.setOnClickListener(v -> attemptRegister());
        tvAlready.setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class))
        );
    }

    private void attemptRegister() {
        String email   = etEmail.getText().toString().trim();
        String pass    = etPassword.getText().toString().trim();
        String confirm = etConfirm.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
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
        if (!pass.equals(confirm)) {
            etConfirm.setError(getString(R.string.err_pass_mismatch));
            return;
        }

        btnSignUp.setEnabled(false);
        viewModel.register(/* username */ "", email, pass);
    }

    private void toast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }
    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
