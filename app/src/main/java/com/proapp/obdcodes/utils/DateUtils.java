package com.proapp.obdcodes.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    /**
     * يحوّل سلسلة ISO (مثلاً "2025-06-16T23:00:58.000000Z")
     * إلى نص منسّق محلياً (مثلاً "16 يونيو 2025" أو "June 16, 2025").
     */
    public static String formatIsoDate(String isoDate, Locale locale) {
        if (isoDate == null || isoDate.isEmpty()) return "";
        // ننشئ مُحلّل للتاريخ بصيغة ISO
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        try {
            Date date = isoFormat.parse(isoDate);
            // نصّ الصيغة: يوم رقم-شهر كامل-سنة، بحسب locale
            SimpleDateFormat outFormat = new SimpleDateFormat("d MMMM yyyy", locale);
            return outFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            // في حال فشل التحويل نُعيد السلسلة الأصلية
            return isoDate;
        }
    }

    /**
     * اختصار: يستخرج الـ Locale من الـ Context ويستدعي formatIsoDate.
     */
    public static String formatIsoDate(Context ctx, String isoDate) {
        Locale locale = ctx.getResources().getConfiguration().getLocales().get(0);
        return formatIsoDate(isoDate, locale);
    }
}
