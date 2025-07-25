package com.eyadalalimi.car.obd2.ui.settings;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import com.eyadalalimi.car.obd2.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // يُحمّل شاشة التفضيلات من ملف XML
        setPreferencesFromResource(R.xml.settings_preferences, rootKey);
    }
}
