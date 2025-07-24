package com.proapp.obdcodes.ui.code_details;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.local.AppDatabase;
import com.proapp.obdcodes.local.entity.ObdCodeEntity;
import com.proapp.obdcodes.network.ApiClient;
import com.proapp.obdcodes.network.model.ObdCode;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.viewmodel.CodeDetailViewModel;

import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CodeDetailsActivity extends BaseActivity {

    private LinearLayout llMeta;
    private TextView tvCode, tvTitle, tvType, tvBrandId,
            tvDescription, tvSymptoms,
            tvCauses, tvSolutions,
            tvSeverity, tvDiagnosis;
    private ImageView ivImage;
    private Button btnSave, btnShare, btnPdf;
    private ObdCode currentCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActivityLayout(R.layout.activity_code_details);
        findViewById(R.id.base_bottom_nav).setVisibility(View.VISIBLE);

        // ربط الواجهات
        llMeta        = findViewById(R.id.llMeta);
        tvCode        = findViewById(R.id.tvCode);
        tvTitle       = findViewById(R.id.tvTitle);
        ivImage       = findViewById(R.id.ivImage);
        tvType        = findViewById(R.id.tvType);
        tvBrandId     = findViewById(R.id.tvBrandId);
        tvDescription = findViewById(R.id.tvDescription);
        tvSymptoms    = findViewById(R.id.tvSymptoms);
        tvCauses      = findViewById(R.id.tvCauses);
        tvSolutions   = findViewById(R.id.tvSolutions);
        tvSeverity    = findViewById(R.id.tvSeverity);
        tvDiagnosis   = findViewById(R.id.tvDiagnosis);
        btnSave       = findViewById(R.id.btnSave);
        btnShare      = findViewById(R.id.btnShare);
        btnPdf        = findViewById(R.id.btnPdf);

        // تعطيل الأزرار مؤقتاً
        btnSave.setEnabled(false);
        btnShare.setEnabled(false);
        btnPdf.setEnabled(false);

        // جلب بيانات Intent
        String code     = getIntent().getStringExtra("CODE");
        String langCode = getIntent().getStringExtra("LANG_CODE");
        tvCode.setText(code);

        // تحميل البيانات Online/Offline
        boolean isOnline = com.proapp.obdcodes.utils.NetworkUtil.isConnected(this);
        if (isOnline) {
            CodeDetailViewModel vm = new ViewModelProvider(this,
                    new ViewModelProvider.AndroidViewModelFactory(getApplication()))
                    .get(CodeDetailViewModel.class);
            vm.loadCodeDetail(code, langCode);
            vm.getCodeDetail().observe(this, this::bindData);
        } else {
            new Thread(() -> {
                ObdCodeEntity ent = AppDatabase.getInstance(getApplicationContext())
                        .obdCodeDao().findByCode(code);
                runOnUiThread(() -> {
                    if (ent != null) bindData(ent.toObdCode());
                    else bindData(null);
                });
            }).start();
        }

        setupButtonListeners();
    }

    private void bindData(@Nullable ObdCode d) {
        if (d == null) {
            Toast.makeText(this, R.string.err_fetch_data, Toast.LENGTH_SHORT).show();
            return;
        }
        currentCode = d;

        tvTitle.setText(d.getTitle());

        // إظهار/إخفاء صف النوع والشركة
        if (d.getBrandId() == null) {
            llMeta.setVisibility(View.GONE);
        } else {
            llMeta.setVisibility(View.VISIBLE);
            tvType.setText(d.getType());
            tvBrandId.setText(String.valueOf(d.getBrandId()));
        }

        // عرض الصورة
        if (d.getImage() != null && !d.getImage().trim().isEmpty()) {
            ivImage.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(ApiClient.IMAGE_BASE_URL + d.getImage())
                    .placeholder(R.drawable.ic_onboarding_1)
                    .error(R.drawable.ic_onboarding_1)
                    .into(ivImage);
        } else {
            ivImage.setImageResource(R.drawable.splash);
        }

        // ملء الحقول المتبقية
        tvDescription.setText(d.getDescription());
        tvSymptoms   .setText(d.getSymptoms());
        tvCauses     .setText(d.getCauses());
        tvSolutions  .setText(d.getSolutions());
        tvSeverity   .setText(d.getSeverity());
        tvDiagnosis  .setText(d.getDiagnosis());

        // تفعيل الأزرار
        btnSave .setEnabled(true);
        btnShare.setEnabled(true);
        btnPdf  .setEnabled(true);
    }

    private void setupButtonListeners() {
        // حفظ
        btnSave.setOnClickListener(v -> {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(this);
            String savedJson = prefs.getString("saved_codes", "[]");
            try {
                JSONArray arr = new JSONArray(savedJson);
                String entry = currentCode.getCode() + "|" +
                        currentCode.getTitle() + "|" +
                        new SimpleDateFormat("yyyy-MM-dd HH:mm")
                                .format(new Date());
                arr.put(entry);
                prefs.edit().putString("saved_codes", arr.toString()).apply();
                Toast.makeText(this, R.string.code_saved, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.err_saving_code, Toast.LENGTH_SHORT).show();
            }
        });

        // مشاركة
        btnShare.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            StringBuilder sb = new StringBuilder()
                    .append("Code: ").append(currentCode.getCode()).append("\n")
                    .append("Title: ").append(currentCode.getTitle()).append("\n\n")
                    .append("Description:\n").append(currentCode.getDescription()).append("\n\n")
                    .append("Symptoms:\n").append(currentCode.getSymptoms()).append("\n\n")
                    .append("Causes:\n").append(currentCode.getCauses()).append("\n\n")
                    .append("Solutions:\n").append(currentCode.getSolutions()).append("\n\n")
                    .append("Diagnosis:\n").append(currentCode.getDiagnosis());
            share.putExtra(Intent.EXTRA_TEXT, sb.toString());
            startActivity(Intent.createChooser(share, getString(R.string.share_code)));
        });

        // توليد PDF
        btnPdf.setOnClickListener(v -> {
            try {
                String fileName = "OBD_" + currentCode.getCode() + ".pdf";
                File mainDir = new File(getExternalFilesDir(null), "OBD Codes/pdf");
                if (!mainDir.exists()) mainDir.mkdirs();
                File file = new File(mainDir, fileName);

                Document doc = new Document();
                PdfWriter.getInstance(doc, new FileOutputStream(file));
                doc.open();
                if (currentCode.getImage() != null && !currentCode.getImage().isEmpty()) {
                    Image img = Image.getInstance(
                            new java.net.URL(ApiClient.IMAGE_BASE_URL + currentCode.getImage()));
                    img.scaleToFit(500, 300);
                    doc.add(img);
                    doc.add(new Paragraph(" "));
                }
                doc.add(new Paragraph("Code: " + currentCode.getCode()));
                doc.add(new Paragraph("Title: " + currentCode.getTitle()));
                doc.add(new Paragraph("Description:\n" + currentCode.getDescription()));
                doc.add(new Paragraph("Symptoms:\n"    + currentCode.getSymptoms()));
                doc.add(new Paragraph("Causes:\n"      + currentCode.getCauses()));
                doc.add(new Paragraph("Solutions:\n"   + currentCode.getSolutions()));
                doc.add(new Paragraph("Diagnosis:\n"   + currentCode.getDiagnosis()));
                doc.close();

                Uri uri = FileProvider.getUriForFile(this,
                        getPackageName() + ".provider", file);
                Intent open = new Intent(Intent.ACTION_VIEW)
                        .setDataAndType(uri, "application/pdf")
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(open, "فتح التقرير باستخدام..."));

            } catch (Exception e) {
                Toast.makeText(this, R.string.err_generating_pdf, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });
    }
}
