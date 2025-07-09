package com.proapp.obdcodes.ui.legal;

import android.os.Bundle;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.base.BaseActivity;

public class PrivacyActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_legal_text); // لا تغيير على اسم الملف

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("سياسة الخصوصية");
    }
}
