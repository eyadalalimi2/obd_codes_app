package com.proapp.obdcodes.ui.settings;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.auth.AuthActivity;

public class SettingsActivity extends AppCompatActivity {

    private Button btnClearCreds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // إيجاد العنصر الوحيد المتبقي
        btnClearCreds = findViewById(R.id.btn_clear_credentials);

        // زر مسح بيانات الدخول (تسجيل خروج)
        btnClearCreds.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("تأكيد المسح")
                    .setMessage("هل تريد حقًا تسجيل الخروج؟")
                    .setPositiveButton("نعم", (d, i) -> {
                        // مسح بيانات الجلسة فقط (SharedPreferences)
                        getSharedPreferences("app_prefs", MODE_PRIVATE)
                                .edit()
                                .clear()
                                .apply();

                        // إعادة توجيه لشاشة تسجيل الدخول وإفراغ الـ back-stack
                        Intent intent = new Intent(this, AuthActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton("لا", null)
                    .show();
        });
    }
}
