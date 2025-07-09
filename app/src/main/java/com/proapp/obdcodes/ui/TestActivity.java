package com.proapp.obdcodes.ui;

import android.content.Intent;
import android.os.Bundle;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.about.AboutActivity;
import com.proapp.obdcodes.ui.account.AccountActivity;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.ui.home.HomeActivity;
import com.proapp.obdcodes.ui.howitworks.HowItWorksActivity;
import com.proapp.obdcodes.ui.pricing.PricingActivity;
import com.proapp.obdcodes.ui.saved.SavedCodesActivity;
import com.proapp.obdcodes.ui.settings.LanguageSettingsActivity;
import com.proapp.obdcodes.ui.support.ContactUsActivity;

public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

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
}
