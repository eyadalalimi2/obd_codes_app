package com.proapp.obdcodes.ui.pdf;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.proapp.obdcodes.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

public class PdfReportAdapter extends ArrayAdapter<File> {

    private final Context context;
    private final List<File> files;

    public PdfReportAdapter(Context ctx, List<File> files) {
        super(ctx, 0, files);
        this.context = ctx;
        this.files = files;
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
            new AlertDialog.Builder(context)
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
        // حفظ إلى التخزين العام
        btnSaveToStorage.setOnClickListener(v -> {
            try {
                File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File targetDir = new File(downloadsDir, "OBD Codes/pdf");

                if (!targetDir.exists()) targetDir.mkdirs();

                File targetFile = new File(targetDir, file.getName());

                FileInputStream in = new FileInputStream(file);
                FileOutputStream out = new FileOutputStream(targetFile);

                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }

                in.close();
                out.close();

                Toast.makeText(context, "تم حفظ التقرير في OBD Codes/pdf", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "فشل في حفظ التقرير", Toast.LENGTH_LONG).show();
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
