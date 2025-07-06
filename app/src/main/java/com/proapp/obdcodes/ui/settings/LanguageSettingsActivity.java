package com.proapp.obdcodes.ui.settings;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.RadioButton;
import androidx.appcompat.app.AppCompatActivity;
import com.proapp.obdcodes.R;

import java.util.Locale;

public class LanguageSettingsActivity extends AppCompatActivity {

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        recreate(); // إعادة تحميل النشاط

        // إعادة تشغيل التطبيق لتطبيق اللغة
        Intent intent = new Intent(this, com.proapp.obdcodes.ui.home.HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_settings);

        RadioButton rbEnglish = findViewById(R.id.rb_english);
        RadioButton rbArabic = findViewById(R.id.rb_arabic);

        rbEnglish.setOnClickListener(v -> setLocale("en"));
        rbArabic.setOnClickListener(v -> setLocale("ar"));
    }
}
