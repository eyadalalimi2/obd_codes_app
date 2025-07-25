package com.eyadalalimi.car.obd2.ui.pricing;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.ui.base.BaseActivity;
import com.eyadalalimi.car.obd2.ui.legal.PrivacyActivity;
import com.eyadalalimi.car.obd2.ui.legal.TermsActivity;

public class PricingActivity extends BaseActivity {

    private Button btnOneTime, btnMonthly, btnYearly;
    private TextView tvPrivacyPolicy, tvTermsOfService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // تحميل الواجهة داخل BaseActivity التي تحتوي على Toolbar + Drawer + BottomNav
        setActivityLayout(R.layout.activity_pricing);

        // لا حاجة لاستدعاء setSupportActionBar مرة أخرى

        // ربط العناصر
        btnOneTime        = findViewById(R.id.btnOneTime);
        btnMonthly        = findViewById(R.id.btnMonthly);
        btnYearly         = findViewById(R.id.btnYearly);
        tvPrivacyPolicy   = findViewById(R.id.tvPrivacyPolicy);
        tvTermsOfService  = findViewById(R.id.tvTermsOfService);

        // الأحداث
        btnOneTime.setOnClickListener(v ->
                Toast.makeText(this, "تم اختيار الخطة لمرة واحدة", Toast.LENGTH_SHORT).show()
        );

        btnMonthly.setOnClickListener(v ->
                Toast.makeText(this, "تم اختيار الاشتراك الشهري", Toast.LENGTH_SHORT).show()
        );

        btnYearly.setOnClickListener(v ->
                Toast.makeText(this, "تم اختيار الاشتراك السنوي", Toast.LENGTH_SHORT).show()
        );

        tvPrivacyPolicy.setOnClickListener(v ->
                startActivity(new Intent(this, PrivacyActivity.class))
        );

        tvTermsOfService.setOnClickListener(v ->
                startActivity(new Intent(this, TermsActivity.class))
        );
    }
    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }
}
