package com.proapp.obdcodes.ui.pdf;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.proapp.obdcodes.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PdfReportActivity extends AppCompatActivity {

    private ListView pdfListView;
    private PdfReportAdapter adapter;
    private List<File> pdfFiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_report);
        setTitle(R.string.nav_pdf);

        pdfListView = findViewById(R.id.pdfListView);
        loadPdfFiles();

        adapter = new PdfReportAdapter(this, pdfFiles);
        pdfListView.setAdapter(adapter);
    }

    private void loadPdfFiles() {
        File dir = new File(getExternalFilesDir(null), "pdf");
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
}
