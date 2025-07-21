package com.proapp.obdcodes.ui.legal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.base.BaseActivity;

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
