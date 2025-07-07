package com.proapp.obdcodes.ui.pdf;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.proapp.obdcodes.R;

import java.io.File;

public class ViewPdfActivity extends AppCompatActivity {

    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);
        setTitle(R.string.view_pdf_page);

        pdfView = findViewById(R.id.pdfView);

        String path = getIntent().getStringExtra("filePath");
        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                pdfView.fromFile(file)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .load();
            }
        }
    }
}
