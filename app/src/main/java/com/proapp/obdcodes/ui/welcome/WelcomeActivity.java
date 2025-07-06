package com.proapp.obdcodes.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.auth.LoginActivity;
import com.proapp.obdcodes.ui.auth.RegisterActivity;

public class WelcomeActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ربط الـ XML بالـ Activity
        setContentView(R.layout.activity_welcome);

        // تهيئة المكونات
        loginButton    = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);

        // حدث الضغط على زرّ تسجيل الدخول
        loginButton.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            // إذا أردت عدم العودة إلى هذه الشاشة، فكّر بإلغاء التعليق:
            // finish();
        });

        // حدث الضغط على زرّ التسجيل
        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class));
            // finish();
        });
    }
}
