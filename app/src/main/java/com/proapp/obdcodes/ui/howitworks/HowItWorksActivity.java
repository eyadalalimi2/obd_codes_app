package com.proapp.obdcodes.ui.howitworks;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.proapp.obdcodes.R;

public class HowItWorksActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_it_works);
        setTitle("كيف يعمل التطبيق؟");

        TextView tv = findViewById(R.id.tvHow);
        tv.setText("كيفية استخدام التطبيق:\n\n1. قم بتوصيل جهاز OBD-II إلى سيارتك.\n2. افتح التطبيق واختر كود العطل أو استخدم ميزة التشخيص بالأعراض.\n3. استعرض تفاصيل الكود: الوصف، الأسباب، الحلول.\n4. يمكنك حفظ الكود أو توليد تقرير PDF.\n\nنصائح:\n- تأكد من الاتصال الجيد بـ OBD عبر البلوتوث أو الواي فاي.\n- استخدم الخطط المدفوعة للوصول إلى ميزات متقدمة.");
    }
}
