package com.eyadalalimi.car.obd2.ui.howitworks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.ui.base.BaseActivity;
import com.eyadalalimi.car.obd2.ui.home.HomeActivity;

public class HowItWorksActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_how_it_works);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("كيف يعمل التطبيق؟");
        }

        // زر "جرب الآن"
        Button btnTry = findViewById(R.id.btnTryNow);
        btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // استبدل هذا بأي إجراء فعلي (فتح الصفحة الرئيسية أو ميزة التشخيص)
                Toast.makeText(HowItWorksActivity.this, "سيتم تحويلك إلى تجربة التطبيق...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HowItWorksActivity.this, HomeActivity.class); // ✅
                startActivity(intent);
            }
        });
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return true;
    }
}
