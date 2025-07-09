package com.proapp.obdcodes.ui.settings;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.auth.AuthActivity;
import com.proapp.obdcodes.ui.base.BaseActivity;

public class SettingsActivity extends BaseActivity {


    private Button btnClearCreds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_settings);

        btnClearCreds = findViewById(R.id.btn_clear_credentials);

        btnClearCreds.setOnClickListener(v -> showCustomLogoutDialog());
    }

    private void showCustomLogoutDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_logout, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        btnConfirm.setOnClickListener(v -> {
            // مسح بيانات الجلسة فقط
            getSharedPreferences("app_prefs", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            // التوجيه إلى شاشة تسجيل الدخول
            Intent intent = new Intent(this, AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }

}
