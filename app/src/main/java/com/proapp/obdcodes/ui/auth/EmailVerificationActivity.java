package com.proapp.obdcodes.ui.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.model.VerifyStatusResponse;

public class EmailVerificationActivity extends AppCompatActivity {
    private VerificationViewModel viewModel;
    private Button btnResend, btnCheckStatus;
    private ProgressBar progressBar;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        btnResend      = findViewById(R.id.btn_resend);
        btnCheckStatus = findViewById(R.id.btn_check_status);
        progressBar    = findViewById(R.id.progress_bar);
        tvStatus       = findViewById(R.id.tv_status);

        viewModel = new ViewModelProvider(this).get(VerificationViewModel.class);

        // عند فتح الشاشة: جلب حالة تحقق البريد مباشرة
        fetchAndDisplayStatus();

        // زر إعادة إرسال الرابط
        btnResend.setOnClickListener(v -> {
            setLoading(true);
            viewModel.sendEmailVerification();
        });

        // زر تحقق الحالة
        btnCheckStatus.setOnClickListener(v -> {
            setLoading(true);
            fetchAndDisplayStatus();
        });

        // مراقبة إرسال رابط التفعيل
        viewModel.getSendLinkResponse().observe(this, result -> {
            setLoading(false);
            if (result != null) {
                Toast.makeText(this, result.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.resend_verification_link, Toast.LENGTH_LONG).show();
            }
        });

        // مراقبة حالة التحقق
        viewModel.getStatusResponse().observe(this, status -> {
            setLoading(false);
            if (status != null) {
                updateStatus(status);
            } else {
                tvStatus.setText(R.string.check_verification_status);
            }
        });
    }

    private void fetchAndDisplayStatus() {
        setLoading(true);
        viewModel.getEmailVerifyStatus();
    }

    private void updateStatus(VerifyStatusResponse status) {
        boolean verified = status.isVerified();
        tvStatus.setText(verified ? R.string.verified : R.string.not_verified);

        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean("email_verified", verified)
                .apply();

        btnResend.setVisibility(verified ? View.GONE : View.VISIBLE);
        btnCheckStatus.setVisibility(verified ? View.GONE : View.VISIBLE);

        if (verified) {
            Toast.makeText(this, R.string.email_now_verified, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnResend.setEnabled(!loading);
        btnCheckStatus.setEnabled(!loading);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean verified = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("email_verified", false);
        if (verified) finish();
        // التحقق من الحالة في كل onResume لضمان التحديث عند العودة من خارج التطبيق
        fetchAndDisplayStatus();
    }
}
