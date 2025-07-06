package com.proapp.obdcodes.ui.about;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.proapp.obdcodes.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("حول التطبيق");

        TextView tvAbout = findViewById(R.id.tvAbout);

        tvAbout.setText("تطبيق OBD Codes هو أداة ذكية تساعد المستخدمين في فهم أكواد الأعطال الخاصة بمركباتهم بسهولة.\n\n"
                + "تم تطوير التطبيق بواسطة: اياد.\n\n"
                + "الميزات الأساسية:\n"
                + "- البحث اليدوي عن كود العطل.\n"
                + "- تشخيص بالأعراض.\n"
                + "- حفظ الأكواد ومشاركتها.\n"
                + "- تحليل الذكاء الاصطناعي.\n"
                + "- دعم الوضع غير المتصل بالإنترنت.\n\n"
                + "تم تصميم التطبيق ليكون سهل الاستخدام وسريع في الوصول للمعلومة.\n"
                + "نوصي دائمًا بالتواصل مع فني معتمد قبل تنفيذ أي إصلاح.");
    }
}
