package com.proapp.obdcodes.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.proapp.obdcodes.R;

public class ResetPasswordActivity extends AppCompatActivity {
    private PasswordViewModel vm;
    private EditText etEmail, etToken, etPass, etConfirm;
    private Button btnReset;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etEmail   = findViewById(R.id.etEmail);
        etToken   = findViewById(R.id.etToken);
        etPass    = findViewById(R.id.etPassword);
        etConfirm = findViewById(R.id.etConfirm);
        btnReset  = findViewById(R.id.btnReset);
        progress  = findViewById(R.id.progress);

        vm = new ViewModelProvider(this).get(PasswordViewModel.class);

        vm.getResetResult().observe(this, result -> {
            setLoading(false);
            if (result != null) {
                Toast.makeText(this, result.getMessage(), Toast.LENGTH_LONG).show();
                if (result.isOk()) finish();
            } else {
                Toast.makeText(this, R.string.err_reset, Toast.LENGTH_SHORT).show();
            }
        });

        btnReset.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String token = etToken.getText().toString().trim();
            String pass  = etPass.getText().toString().trim();
            String conf  = etConfirm.getText().toString().trim();

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError(getString(R.string.err_invalid_email));
                return;
            }
            if (TextUtils.isEmpty(token)) {
                etToken.setError(getString(R.string.hint_token));
                return;
            }
            if (pass.length() < 8) {
                etPass.setError(getString(R.string.err_invalid_pass));
                return;
            }
            if (!pass.equals(conf)) {
                etConfirm.setError(getString(R.string.err_not_match));
                return;
            }
            setLoading(true);
            vm.resetPassword(email, token, pass, conf);
        });
    }

    private void setLoading(boolean loading) {
        progress.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnReset.setEnabled(!loading);
    }
}
