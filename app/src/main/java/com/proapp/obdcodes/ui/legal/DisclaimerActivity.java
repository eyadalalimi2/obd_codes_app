package com.proapp.obdcodes.ui.legal;

import android.os.Bundle;
import android.widget.TextView;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.base.BaseActivity;

public class DisclaimerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_disclaimer); // الاحتفاظ بالاسم كما هو

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("إخلاء المسؤولية");

        TextView tvDisclaimer = findViewById(R.id.tvDisclaimer);
        tvDisclaimer.setText("تنويه: هذا التطبيق لأغراض معرفية فقط.\n\n" +
                "لا يتحمل المطور أي مسؤولية عن استخدام النتائج الواردة في التطبيق لتشخيص أو إصلاح المركبات.\n" +
                "يجب على المستخدم دائمًا الرجوع إلى مختص معتمد للتأكد من سبب العطل والإصلاح الصحيح.\n\n" +
                "المعلومات المعروضة مستندة إلى مصادر عامة وليست مخصصة لعلامة تجارية معينة.");
    }
}
