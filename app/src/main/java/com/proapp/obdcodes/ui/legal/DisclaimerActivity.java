package com.proapp.obdcodes.ui.legal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.ui.support.ContactUsActivity;

public class DisclaimerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_disclaimer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("إخلاء المسؤولية");

        // زر الدعم السريع - ينتقل إلى صفحة "تواصل بنا"
        Button btnContact = findViewById(R.id.btn_contact_support);
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // الانتقال إلى صفحة التواصل (استبدل ContactUsActivity إذا كان اسم مختلف)
                Intent i = new Intent(DisclaimerActivity.this, ContactUsActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return true;
    }
}
