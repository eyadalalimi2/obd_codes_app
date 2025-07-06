package com.proapp.obdcodes.ui.auth;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.proapp.obdcodes.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnSendLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setTitle(getString(R.string.forgot_password_title));

        etEmail = findViewById(R.id.etEmail);
        btnSendLink = findViewById(R.id.btnSendLink);

        btnSendLink.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, R.string.please_enter_email, Toast.LENGTH_SHORT).show();
            } else {
                // محاكاة إرسال رابط استعادة كلمة المرور
                String msg = getString(R.string.reset_link_sent, email);
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
