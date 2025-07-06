package com.proapp.obdcodes.ui.pricing;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.ui.legal.PrivacyActivity;
import com.proapp.obdcodes.ui.legal.TermsActivity;

public class PricingActivity extends BaseActivity {

    private Button btnOneTime, btnMonthly, btnYearly;
    private TextView tvPrivacyPolicy, tvTermsOfService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_pricing);

        // Toolbar + hamburger
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // ربط الأزرار
        btnOneTime        = findViewById(R.id.btnOneTime);
        btnMonthly        = findViewById(R.id.btnMonthly);
        btnYearly         = findViewById(R.id.btnYearly);
        tvPrivacyPolicy   = findViewById(R.id.tvPrivacyPolicy);
        tvTermsOfService  = findViewById(R.id.tvTermsOfService);

        // بيانات وهمية مؤقتة عند اختيار الخطة
        btnOneTime.setOnClickListener(v ->
                Toast.makeText(this, "One-time plan selected", Toast.LENGTH_SHORT).show()
        );
        btnMonthly.setOnClickListener(v ->
                Toast.makeText(this, "Monthly subscription selected", Toast.LENGTH_SHORT).show()
        );
        btnYearly.setOnClickListener(v ->
                Toast.makeText(this, "Yearly subscription selected", Toast.LENGTH_SHORT).show()
        );

        // فتح صفحة سياسة الخصوصية
        tvPrivacyPolicy.setOnClickListener(v ->
                startActivity(new Intent(this, PrivacyActivity.class))
        );

        // فتح صفحة شروط الاستخدام
        tvTermsOfService.setOnClickListener(v ->
                startActivity(new Intent(this, TermsActivity.class))
        );
    }
}
