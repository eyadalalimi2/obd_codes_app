package com.eyadalalimi.car.obd2.ui.support;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.ui.base.BaseActivity;
import com.eyadalalimi.car.obd2.ui.howitworks.HowItWorksActivity;
import com.google.android.material.snackbar.Snackbar;

public class ContactUsActivity extends BaseActivity {

    private EditText etSubject, etMessage;
    private Button btnSend;
    private LinearLayout llHowItWorks, llEmailLink;
    private TextView tvSupportEmail;
    private View progressView; // وهمي لإظهار Progress داخل الزر

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_contact_us);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // ربط الحقول والأزرار
        etSubject      = findViewById(R.id.etSubject);
        etMessage      = findViewById(R.id.etMessage);
        btnSend        = findViewById(R.id.btnSend);
        llEmailLink    = findViewById(R.id.llEmailLink);
        llHowItWorks   = findViewById(R.id.llHowItWorks);
        tvSupportEmail = findViewById(R.id.tvSupportEmail);
        progressView   = findViewById(R.id.progressView);

        // جعل البريد الإلكتروني قابل للنقر
        tvSupportEmail.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:support@obdquest.com"));
            startActivity(Intent.createChooser(emailIntent, getString(R.string.send_via)));
        });

        // زر إرسال مع progress وسنackbar
        btnSend.setOnClickListener(v -> {
            String subject = etSubject.getText().toString().trim();
            String message = etMessage.getText().toString().trim();
            if (TextUtils.isEmpty(subject) || TextUtils.isEmpty(message)) {
                Snackbar.make(btnSend, R.string.fill_all_fields, Snackbar.LENGTH_SHORT).show();
                return;
            }
            // إظهار progress داخل الزر
            btnSend.setEnabled(false);
            progressView.setVisibility(View.VISIBLE);

            // محاكاة إرسال (يفضل هنا تكامل API فعلي لاحقًا)
            new Handler().postDelayed(() -> {
                progressView.setVisibility(View.GONE);
                btnSend.setEnabled(true);

                // فتح تطبيق البريد لإرسال الرسالة
                Intent email = new Intent(Intent.ACTION_SENDTO,
                        Uri.parse("mailto:support@obdcode.xyz"));
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(email, getString(R.string.send_via)));

                Snackbar.make(btnSend, R.string.message_sent_success, Snackbar.LENGTH_LONG).show();
            }, 1300);
        });

        // روابط الدعم السفلي
        llEmailLink.setOnClickListener(v -> {
            Intent email = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:support@obdcode.xyz"));
            startActivity(Intent.createChooser(email, getString(R.string.send_via)));
        });
        llHowItWorks.setOnClickListener(v -> startActivity(new Intent(this, HowItWorksActivity.class)));
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return true;
    }
}
