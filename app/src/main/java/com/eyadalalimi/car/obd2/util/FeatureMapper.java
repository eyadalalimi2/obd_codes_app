// com.proapp.obdcodes.util/FeatureMapper.java
package com.eyadalalimi.car.obd2.util;

import java.util.HashMap;
import java.util.Map;

public final class FeatureMapper {
    private static final Map<String, String> MAP = new HashMap<>();
    static {
        MAP.put("SEARCH_CODES",          "البحث بالأكواد");
        MAP.put("SAVE_CODES",            "حفظ الأكواد");
        MAP.put("SHARE_CODES",           "مشاركة الأكواد");
        MAP.put("COMPARE_CODES",         "مقارنة الأكواد");
        MAP.put("OFFLINE_MODE",         "وضع الاتصال بدون انترنت");
        MAP.put("DIAGNOSIS_HISTORY",     "سجل التشخيص");
        MAP.put("SYMPTOM_BASED_DIAGNOSIS","التشخيص بالأعراض");
        MAP.put("SMART_NOTIFICATIONS",   "الإشعارات الذكية");
        MAP.put("PDF_REPORT",            "توليد تقارير PDF");
        MAP.put("TRENDING_CODES_ANALYTICS"," الأكواد الشائعة");
        MAP.put("VISUAL_COMPONENT_LIBRARY","المكتبة المرئية");
        MAP.put("AI_DIAGNOSTIC_ASSISTANT","مساعد الذكاء الاصطناعي");
        // … أضف باقي المفاتيح هنا
    }

    public static String toReadable(String key) {
        String label = MAP.get(key);
        return label != null ? label : key;  // إذا لم يُعرّف، عرض المفتاح نفسه
    }
}
