package com.proapp.obdcodes.ui.settings;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.RadioButton;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.ui.home.HomeActivity;

import java.util.Locale;

public class LanguageSettingsActivity extends BaseActivity {

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // إعادة تشغيل التطبيق لتطبيق اللغة الجديدة
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_language_settings); // مهم

        setTitle("إعدادات اللغة"); // يمكن ترجمته عبر strings.xml

        RadioButton rbEnglish = findViewById(R.id.rb_english);
        RadioButton rbArabic = findViewById(R.id.rb_arabic);

        rbEnglish.setOnClickListener(v -> setLocale("en"));
        rbArabic.setOnClickListener(v -> setLocale("ar"));
    }
}
