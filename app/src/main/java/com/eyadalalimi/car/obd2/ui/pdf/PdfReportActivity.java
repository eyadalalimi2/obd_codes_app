package com.eyadalalimi.car.obd2.ui.pdf;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.ui.base.BaseActivity;
import com.eyadalalimi.car.obd2.util.SubscriptionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PdfReportActivity extends BaseActivity {

    private static final int REQUEST_STORAGE_PERMISSION = 101;

    private ListView pdfListView;
    private PdfReportAdapter adapter;
    private List<File> pdfFiles = new ArrayList<>();
    private File fileToSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // حماية الميزة PDF_REPORT قبل تهيئة الواجهة
        SubscriptionUtils.checkFeatureAccess(this, "PDF_REPORT", () -> runOnUiThread(() -> {
            setActivityLayout(R.layout.activity_pdf_report);
            setTitle(R.string.nav_pdf);

            pdfListView = findViewById(R.id.pdfListView);
            loadPdfFiles();

            adapter = new PdfReportAdapter(this, pdfFiles, this::checkStoragePermissionAndSave);
            pdfListView.setAdapter(adapter);

            pdfListView.setOnItemClickListener((parent, view, position, id) -> {
                File file = pdfFiles.get(position);
                openPdfExternally(file);
            });
        }));
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
            android.net.Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "تعذر فتح الملف", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkStoragePermissionAndSave(File file) {
        this.fileToSave = file;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_STORAGE_PERMISSION);
                return;
            }
        }
        saveFileToStorage(fileToSave);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveFileToStorage(fileToSave);
            } else {
                Toast.makeText(this, "الصلاحية ضرورية لحفظ التقرير", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void saveFileToStorage(File file) {
        try {
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File targetDir = new File(downloadsDir, "OBD Codes/pdf");

            if (!targetDir.exists()) targetDir.mkdirs();

            File targetFile = new File(targetDir, file.getName());

            try (FileInputStream in = new FileInputStream(file);
                 FileOutputStream out = new FileOutputStream(targetFile)) {

                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
            }

            Toast.makeText(this, "تم حفظ التقرير في OBD Codes/pdf", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "فشل في حفظ التقرير", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }
}
