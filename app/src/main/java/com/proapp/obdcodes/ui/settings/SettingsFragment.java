package com.proapp.obdcodes.ui.settings;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import com.proapp.obdcodes.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // يُحمّل شاشة التفضيلات من ملف XML
        setPreferencesFromResource(R.xml.settings_preferences, rootKey);
    }
}
