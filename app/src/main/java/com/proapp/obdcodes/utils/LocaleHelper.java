package com.proapp.obdcodes.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import java.util.Locale;

public class LocaleHelper {
    public static void setLocale(Context ctx, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration cfg = ctx.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cfg.setLocale(locale);
            ctx.createConfigurationContext(cfg);
        } else {
            cfg.locale = locale;
            ctx.getResources().updateConfiguration(cfg, ctx.getResources().getDisplayMetrics());
        }
    }
}
