package com.eyadalalimi.car.obd2.ui.pdf;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.eyadalalimi.car.obd2.R;

import java.io.File;
import java.util.List;

public class PdfReportAdapter extends ArrayAdapter<File> {

    public interface OnSavePdfListener {
        void onSavePdf(File file);
    }

    private final Context context;
    private final List<File> files;
    private final OnSavePdfListener saveListener;

    public PdfReportAdapter(Context ctx, List<File> files, OnSavePdfListener listener) {
        super(ctx, 0, files);
        this.context = ctx;
        this.files = files;
        this.saveListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        File file = files.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pdf_report, parent, false);
        }

        TextView tvFileName = convertView.findViewById(R.id.tvName);
        ImageButton btnOpen = convertView.findViewById(R.id.btnOpenPdf);
        ImageButton btnShare = convertView.findViewById(R.id.btnSharePdf);
        ImageButton btnDelete = convertView.findViewById(R.id.btnDeletePdf);
        ImageButton btnSaveToStorage = convertView.findViewById(R.id.btnSaveToStorage);

        tvFileName.setText(file.getName());

        btnOpen.setOnClickListener(v -> openPdfExternally(file));

        btnShare.setOnClickListener(v -> {
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("application/pdf");
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(share, "مشاركة التقرير"));
        });

        btnDelete.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(context)
                    .setTitle("تأكيد الحذف")
                    .setMessage("هل تريد حذف هذا التقرير؟")
                    .setPositiveButton("نعم", (dialog, which) -> {
                        if (file.delete()) {
                            files.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(context, "تم حذف الملف", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "فشل في الحذف", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("إلغاء", null)
                    .show();
        });

        btnSaveToStorage.setOnClickListener(v -> {
            if (saveListener != null) {
                saveListener.onSavePdf(file);
            }
        });

        return convertView;
    }

    private void openPdfExternally(File file) {
        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intent, "فتح التقرير"));
    }
}
