package com.eyadalalimi.car.obd2.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.eyadalalimi.car.obd2.R;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button loginBtn      = findViewById(R.id.loginButton);
        TextView registerBtn = findViewById(R.id.registerButton);

        loginBtn.setOnClickListener(v ->
                startActivity(new Intent(this, AuthActivity.class))
        );

        registerBtn.setOnClickListener(v ->
                startActivity(new Intent(this, AuthActivity.class))
        );
    }
}
