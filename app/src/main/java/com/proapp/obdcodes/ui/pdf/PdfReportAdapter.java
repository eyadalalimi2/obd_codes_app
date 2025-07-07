package com.proapp.obdcodes.ui.pdf;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.proapp.obdcodes.R;

import java.io.File;
import java.util.List;

public class PdfReportAdapter extends ArrayAdapter<File> {

    private Context context;
    private List<File> files;

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

        tvFileName.setText(file.getName());

        btnOpen.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewPdfActivity.class);
            intent.putExtra("filePath", file.getAbsolutePath());
            context.startActivity(intent);
        });

        btnShare.setOnClickListener(v -> {
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("application/pdf");
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(share, "مشاركة الملف"));
        });

        btnDelete.setOnClickListener(v -> {
            if (file.delete()) {
                files.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
