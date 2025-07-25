package com.eyadalalimi.car.obd2.ui.settings;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.RadioButton;

import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.ui.base.BaseActivity;
import com.eyadalalimi.car.obd2.ui.home.HomeActivity;

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
        RadioButton rbFrench = findViewById(R.id.rb_french);
        RadioButton rbTurkish = findViewById(R.id.rb_turkish);
        RadioButton rbRussian = findViewById(R.id.rb_russian);

        rbEnglish.setOnClickListener(v -> setLocale("en"));
        rbArabic.setOnClickListener(v -> setLocale("ar"));
        rbFrench.setOnClickListener(v -> setLocale("fr"));
        rbTurkish.setOnClickListener(v -> setLocale("tr"));
        rbRussian.setOnClickListener(v -> setLocale("ru"));
    }
    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }

}
