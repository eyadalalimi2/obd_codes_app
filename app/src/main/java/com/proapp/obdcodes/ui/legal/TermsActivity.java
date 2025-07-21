package com.proapp.obdcodes.ui.legal;

import android.os.Bundle;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.base.BaseActivity;

public class TermsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_terms);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("شروط الخدمة");
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return true;
    }
}
