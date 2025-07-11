package com.proapp.obdcodes.ui.pdf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.util.SubscriptionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PdfReportActivity extends BaseActivity {

    private ListView pdfListView;
    private PdfReportAdapter adapter;
    private List<File> pdfFiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_pdf_report);
        setTitle(R.string.nav_pdf);
        // ✅ تحقق من صلاحية استخدام ميزة تقارير PDF
        SubscriptionUtils.hasFeature(this, "PDF_REPORT", isAllowed -> {
            if (!isAllowed) {
                Toast.makeText(this, "هذه الميزة متاحة فقط للمشتركين", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            // ✅ السماح بالوصول
            pdfListView = findViewById(R.id.pdfListView);
            loadPdfFiles();

            adapter = new PdfReportAdapter(this, pdfFiles);
            pdfListView.setAdapter(adapter);
        });
    }

    private void loadPdfFiles() {
        File dir = new File(getExternalFilesDir(null), "OBD Codes/pdf");
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.getName().endsWith(".pdf")) {
                        pdfFiles.add(f);
                    }
                }
            }
        }
    }

    private void openPdfExternally(File file) {
        try {
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "تعذر فتح الملف", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }

}
