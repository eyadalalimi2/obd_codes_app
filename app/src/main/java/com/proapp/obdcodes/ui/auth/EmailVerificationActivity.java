package com.proapp.obdcodes.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.model.VerifyStatusResponse;
import com.proapp.obdcodes.ui.auth.VerificationViewModel;
import com.proapp.obdcodes.ui.home.HomeActivity;

public class EmailVerificationActivity extends AppCompatActivity {
    private VerificationViewModel viewModel;
    private Button btnResend, btnCheckStatus;
    private ProgressBar progressBar;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        // 1. ربط عناصر الواجهة
        btnResend      = findViewById(R.id.btn_resend);
        btnCheckStatus = findViewById(R.id.btn_check_status);
        progressBar    = findViewById(R.id.progress_bar);
        tvStatus       = findViewById(R.id.tv_status);

        // 2. تهيئة ViewModel
        viewModel = new ViewModelProvider(this)
                .get(VerificationViewModel.class);

        // 3. مراقبة نتيجة إعادة إرسال رابط التفعيل
        viewModel.getSendLinkResponse().observe(this, response -> {
            setLoading(false);
            String message = (response != null && response.getMessage() != null)
                    ? response.getMessage()
                    : getString(R.string.resend_verification_link);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });

        // 4. مراقبة حالة التحقق
        viewModel.getStatusResponse().observe(this, status -> {
            setLoading(false);
            if (status != null) {
                boolean verified = status.isVerified();
                tvStatus.setText(verified
                        ? R.string.verified
                        : R.string.not_verified
                );
                btnResend.setVisibility(verified ? View.GONE : View.VISIBLE);
                btnCheckStatus.setVisibility(verified ? View.GONE : View.VISIBLE);

                if (verified) {
                    Toast.makeText(this, R.string.email_now_verified, Toast.LENGTH_SHORT).show();

                    // انتقل إلى صفحة الحساب الرئيسية ونظف الستاك
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            } else {
                tvStatus.setText(R.string.check_verification_status);
            }
        });

        // 5. زر إعادة إرسال رابط التفعيل
        btnResend.setOnClickListener(v -> {
            setLoading(true);
            viewModel.sendEmailVerification();
        });

        // 6. زر التحقق من الحالة يدوياً
        btnCheckStatus.setOnClickListener(v -> {
            setLoading(true);
            viewModel.fetchEmailVerifyStatus();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // عند عودة الشاشة للخلفية، نجدد حالة التحقق
        setLoading(true);
        viewModel.fetchEmailVerifyStatus();
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnResend.setEnabled(!loading);
        btnCheckStatus.setEnabled(!loading);
    }
}
