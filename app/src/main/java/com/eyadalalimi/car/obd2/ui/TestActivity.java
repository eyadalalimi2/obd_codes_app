package com.eyadalalimi.car.obd2.ui;

import android.content.Intent;
import android.os.Bundle;
import com.eyadalalimi.car

        .obd2.R;
import com.eyadalalimi.car.obd2.ui.about.AboutActivity;
import com.eyadalalimi.car.obd2.ui.account.AccountActivity;
import com.eyadalalimi.car.obd2.ui.base.BaseActivity;
import com.eyadalalimi.car.obd2.ui.home.HomeActivity;
import com.eyadalalimi.car.obd2.ui.howitworks.HowItWorksActivity;
import com.eyadalalimi.car.obd2.ui.pricing.PricingActivity;
import com.eyadalalimi.car.obd2.ui.saved.SavedCodesActivity;
import com.eyadalalimi.car.obd2.ui.settings.LanguageSettingsActivity; // تأكد من أن هذا النشاط يستخدم BaseActivity أيضًا
import com.eyadalalimi.car.obd2.ui.support.ContactUsActivity;

public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ✅ استبدل setContentView بـ setActivityLayout للسماح لـ BaseActivity بتهيئة الـ Drawer/BottomNav/Toolbar
        setActivityLayout(R.layout.activity_test);

        // ✅ قم بضبط عنوان شريط الأدوات لهذا النشاط
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("لوحة التحكم"); // أو أي عنوان مناسب
        }

        findViewById(R.id.btn_language).setOnClickListener(v ->
                startActivity(new Intent(this, LanguageSettingsActivity.class)));

        findViewById(R.id.btn_home).setOnClickListener(v ->
                startActivity(new Intent(this, HomeActivity.class)));

        findViewById(R.id.btn_saved).setOnClickListener(v ->
                startActivity(new Intent(this, SavedCodesActivity.class)));

        findViewById(R.id.btn_account).setOnClickListener(v ->
                startActivity(new Intent(this, AccountActivity.class)));

        findViewById(R.id.btn_about).setOnClickListener(v ->
                startActivity(new Intent(this, AboutActivity.class)));

        findViewById(R.id.btn_contact).setOnClickListener(v ->
                startActivity(new Intent(this, ContactUsActivity.class)));

        findViewById(R.id.btn_how).setOnClickListener(v ->
                startActivity(new Intent(this, HowItWorksActivity.class)));

        findViewById(R.id.btn_pricing).setOnClickListener(v ->
                startActivity(new Intent(this, PricingActivity.class)));
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return true; // ✅ إذا كنت تريد إظهار BottomNav في لوحة التحكم، اجعلها true.
        // إذا كانت هذه الشاشة تمثل "Dashboard" في BottomNav، فيجب أن تكون true.
    }
}