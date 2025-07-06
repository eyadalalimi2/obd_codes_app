package com.proapp.obdcodes.ui.legal;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.proapp.obdcodes.R;

public class DisclaimerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);
        setTitle("إخلاء المسؤولية");

        TextView tvDisclaimer = findViewById(R.id.tvDisclaimer);

        tvDisclaimer.setText("تنويه: هذا التطبيق لأغراض معرفية فقط.\n\n" +
                "لا يتحمل المطور أي مسؤولية عن استخدام النتائج الواردة في التطبيق لتشخيص أو إصلاح المركبات.\n" +
                "يجب على المستخدم دائمًا الرجوع إلى مختص معتمد للتأكد من سبب العطل والإصلاح الصحيح.\n\n" +
                "المعلومات المعروضة مستندة إلى مصادر عامة وليست مخصصة لعلامة تجارية معينة.");
    }
}
