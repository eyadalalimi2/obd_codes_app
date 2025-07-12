// com.proapp.obdcodes.network.model/Subscription.java
package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.List;

public class Subscription {
    @SerializedName("id")
    private long id;
    // الاستجابة تضمّ حقل plan فيه بيانات الباقة
    @SerializedName("plan")
    private Plan plan;

    @SerializedName("start_at")
    private String startAt;

    @SerializedName("end_at")
    private String endAt;

    @SerializedName("status")
    private String status;   // "active" أو "expired" أو ما شابه

    // ----------------- Getters -----------------
    public long getId() { return id; }
    public Plan getPlan() {
        return plan;
    }

    public String getStatus() {
        return status;
    }

    /** صيغة العرض لاسم الباقة */
    public String getName() {
        return plan != null ? plan.getName() : null;
    }

    /** السعر من داخل الـ plan */
    public String getFormattedPrice() {
        return plan != null ? plan.getFormattedPrice() : "-";
    }

    /** قائمة مفاتيح المميزات */
    public List<String> getFeatures() {
        return plan != null ? plan.getFeatures() : null;
    }

    /** نصّ المميزات */
    public String getFeaturesText() {
        if (plan == null || plan.getFeatures() == null || plan.getFeatures().isEmpty()) {
            return "لا توجد مزايا";
        }
        // تحوّل المفاتيح إلى نصوص قابلة للقراءة
        return "- " + String.join("\n- ", plan.getFeatures());
    }

    // جديد: معرّف المنتج في Google Play
    @SerializedName("google_product_id")
    private String googleProductId;
    // ----------------- تواريخ -----------------

    public String getStartAt() {
        return startAt;
    }

    public String getEndAt() {
        return endAt;
    }
    public String getGoogleProductId() { return googleProductId; }
    /**
     * تاريخ البداية بصيغة مقروءة (yyyy-MM-dd أو حسب الحاجة)
     */
    public String getStartDateFormatted() {
        if (startAt == null) return "-";
        try {
            // لو تبيّا فقط الجزء الأول yyyy-MM-dd
            return startAt.split("T")[0];
        } catch (Exception e) {
            return startAt;
        }
    }

    /**
     * تاريخ الانتهاء بصيغة مقروءة
     */
    public String getEndDateFormatted() {
        if (endAt == null) return "-";
        try {
            return endAt.split("T")[0];
        } catch (Exception e) {
            return endAt;
        }
    }

    /**
     * الأيام المتبقية من اليوم حتى نهاية الاشتراك
     */
    public int getDaysLeft() {
        if (endAt == null) return 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            Date endDate = sdf.parse(getEndDateFormatted());
            long diffMs = endDate.getTime() - now.getTime();
            return (int) TimeUnit.DAYS.convert(diffMs, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return 0;
        }
    }
}
