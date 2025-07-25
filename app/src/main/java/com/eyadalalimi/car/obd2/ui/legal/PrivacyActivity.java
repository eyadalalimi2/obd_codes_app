package com.eyadalalimi.car.obd2.ui.legal;

import android.os.Bundle;

import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.ui.base.BaseActivity;

public class PrivacyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_privacy_policy);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("سياسة الخصوصية");


    }

    @Override
    protected boolean shouldShowBottomNav() {
        return true;
    }
}
