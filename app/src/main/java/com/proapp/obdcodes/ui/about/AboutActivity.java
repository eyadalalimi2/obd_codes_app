package com.proapp.obdcodes.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.base.BaseActivity;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_about);

        setTitle("حول التطبيق");

        // زر مشاركة التطبيق
        Button btnShare = findViewById(R.id.btnShareApp);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareText = "جرّب تطبيق OBD Codes لمعرفة أكواد الأعطال، حمله الآن:\nhttps://play.google.com/store/apps/details?id=com.proapp.obdcodes";
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                startActivity(Intent.createChooser(shareIntent, "مشاركة التطبيق"));
            }
        });

        // اسم المطور (يفتح البريد أو موقع المطور)
        TextView tvDeveloper = findViewById(R.id.tvDeveloper);
        tvDeveloper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // بريد المطور أو موقع إلكتروني - عدل حسب رغبتك
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:eyadads.inc@gmail.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "OBD Codes App - تواصل");
                startActivity(Intent.createChooser(emailIntent, "مراسلة المطور"));
                // أو لفتح موقع:
                // Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://iyad.dev"));
                // startActivity(browserIntent);
            }
        });
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }
}
