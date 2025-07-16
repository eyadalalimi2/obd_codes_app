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

public class ForgotPasswordActivity extends AppCompatActivity {
    private PasswordViewModel vm;
    private EditText etEmail;
    private Button btnSend;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail  = findViewById(R.id.etEmail);
        btnSend  = findViewById(R.id.btnSendLink);
        progress = findViewById(R.id.progress);

        vm = new ViewModelProvider(this).get(PasswordViewModel.class);

        vm.getForgotResult().observe(this, result -> {
            setLoading(false);
            if (result != null) {
                Toast.makeText(this, result.getMessage(), Toast.LENGTH_LONG).show();
                if (result.isOk()) finish();
            } else {
                Toast.makeText(this, R.string.err_send_link, Toast.LENGTH_SHORT).show();
            }
        });

        btnSend.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError(getString(R.string.err_invalid_email));
                return;
            }
            setLoading(true);
            vm.forgotPassword(email);
        });
    }

    private void setLoading(boolean loading) {
        progress.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnSend.setEnabled(!loading);
    }
}
