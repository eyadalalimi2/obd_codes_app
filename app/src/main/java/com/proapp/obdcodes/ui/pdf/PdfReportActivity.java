package com.proapp.obdcodes.ui.pdf;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.proapp.obdcodes.R;

public class PdfReportActivity extends AppCompatActivity {

    private TextView tvSummary;
    private Button btnSavePdf, btnSharePdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_report);
        setTitle("تقرير PDF");

        tvSummary = findViewById(R.id.tvSummary);
        btnSavePdf = findViewById(R.id.btnSavePdf);
        btnSharePdf = findViewById(R.id.btnSharePdf);

        // محتوى تجريبي
        tvSummary.setText("تقرير الكود:\n\n" +
                "الكود: P0300\n" +
                "الوصف: خلل عشوائي في الاشتعال\n" +
                "الأسباب: شمعات الاشتعال، أسلاك الإشعال\n" +
                "الحلول: فحص واستبدال الشمعات");

        btnSavePdf.setOnClickListener(v ->
                Toast.makeText(this, "تم حفظ التقرير (وهمي)", Toast.LENGTH_SHORT).show());

        btnSharePdf.setOnClickListener(v ->
                Toast.makeText(this, "تمت المشاركة (وهمي)", Toast.LENGTH_SHORT).show());
    }
}
