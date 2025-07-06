package com.proapp.obdcodes.ui.support;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.ui.howitworks.HowItWorksActivity;

public class ContactUsActivity extends BaseActivity {
    
    private EditText etSubject, etMessage;
    private Button btnSend;
    private LinearLayout llEmailLink, llHowItWorks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_contact_us);

        // Toolbar + hamburger
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // ربط الحقول والأزرار
        etSubject      = findViewById(R.id.etSubject);
        etMessage      = findViewById(R.id.etMessage);
        btnSend        = findViewById(R.id.btnSend);
        llEmailLink    = findViewById(R.id.llEmailLink);
        llHowItWorks   = findViewById(R.id.llHowItWorks);

        // إرسال النموذج عبر بريد
        btnSend.setOnClickListener(v -> {
            String subject = etSubject.getText().toString().trim();
            String message = etMessage.getText().toString().trim();
            if (subject.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                return;
            }
            Intent email = new Intent(Intent.ACTION_SENDTO,
                    Uri.parse("mailto:support@obdquest.com"));
            email.putExtra(Intent.EXTRA_SUBJECT, subject);
            email.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(email, getString(R.string.send_via)));
        });

        // رابط البريد
        llEmailLink.setOnClickListener(v -> {
            Intent email = new Intent(Intent.ACTION_SENDTO,
                    Uri.parse("mailto:support@obdquest.com"));
            startActivity(Intent.createChooser(email, getString(R.string.send_via)));
        });

        // رابط How it works
        llHowItWorks.setOnClickListener(v ->
                startActivity(new Intent(this, HowItWorksActivity.class))
        );
    }
}
