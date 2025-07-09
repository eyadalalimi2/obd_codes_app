package com.proapp.obdcodes.ui.howitworks;

import android.os.Bundle;
import android.widget.TextView;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.base.BaseActivity;

public class HowItWorksActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_how_it_works); // ✅ تحميل المحتوى داخل base layout

        // إعداد عنوان الصفحة في شريط الأدوات
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("كيف يعمل التطبيق؟");
        }

        // ربط عنصر النص وعرض المحتوى
        TextView tv = findViewById(R.id.tvHow);
        tv.setText("كيفية استخدام التطبيق:\n\n" +
                "1. قم بتوصيل جهاز OBD-II إلى سيارتك.\n" +
                "2. افتح التطبيق واختر كود العطل أو استخدم ميزة التشخيص بالأعراض.\n" +
                "3. استعرض تفاصيل الكود: الوصف، الأسباب، الحلول.\n" +
                "4. يمكنك حفظ الكود أو توليد تقرير PDF.\n\n" +
                "نصائح:\n" +
                "- تأكد من الاتصال الجيد بـ OBD عبر البلوتوث أو الواي فاي.\n" +
                "- استخدم الخطط المدفوعة للوصول إلى ميزات متقدمة.");
    }
    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }

}
