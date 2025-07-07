package com.proapp.obdcodes.ui.code_details;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

        // 1) حقن layout داخل BaseActivity
        setActivityLayout(R.layout.activity_code_details);

        // 2) إخفاء BottomNav في شاشة التفاصيل
        findViewById(R.id.base_bottom_nav).setVisibility(View.GONE);

        // 3) ربط عناصر الواجهة
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

        btnSave  = findViewById(R.id.btnSave);
        btnShare = findViewById(R.id.btnShare);
        btnPdf   = findViewById(R.id.btnPdf);

        // 4) تعطيل الأزرار حتى يتم التحميل
        btnSave.setEnabled(false);
        btnShare.setEnabled(false);
        btnPdf.setEnabled(false);

        // 5) جلب الكود من intent
        String code = getIntent().getStringExtra("CODE");

        // 6) تهيئة ViewModel وتحميل البيانات
        CodeDetailViewModel viewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication()))
                .get(CodeDetailViewModel.class);
        viewModel.loadCodeDetail(code);
        viewModel.getCodeDetail().observe(this, this::bindData);

        // 7) إعداد مستمعات الأزرار
        setupButtonListeners();
    }

    private void bindData(@Nullable ObdCode d) {
        if (d == null) {
            tvCode.setText(R.string.err_fetch_data);
            return;
        }

        currentCode = d;

        tvCode.setText(d.getCode());
        tvTitle.setText(d.getTitle());

        // عرض الصورة إن وجدت، أو عرض صورة افتراضية في حال عدم وجودها
        if (d.getImage() != null && !d.getImage().trim().isEmpty()) {
            ivImage.setVisibility(ImageView.VISIBLE);
            Glide.with(this)
                    .load(ApiClient.IMAGE_BASE_URL + d.getImage())
                    .placeholder(R.drawable.ic_onboarding_1)  // صورة افتراضية أثناء التحميل
                    .error(R.drawable.ic_onboarding_1)        // صورة افتراضية عند الخطأ
                    .into(ivImage);
        } else {
            ivImage.setVisibility(ImageView.VISIBLE);  // تأكد أن الصورة مرئية
            ivImage.setImageResource(R.drawable.splash); // تعيين صورة افتراضية
        }

        // بيانات الكود
        tvType.setText(d.getType());
        tvBrandId.setText(d.getBrandId() != null
                ? String.valueOf(d.getBrandId()) : "-");
        tvDescription.setText(d.getDescription());
        tvSymptoms.setText(d.getSymptoms());
        tvCauses.setText(d.getCauses());
        tvSolutions.setText(d.getSolutions());
        tvSeverity.setText(d.getSeverity());
        tvDiagnosis.setText(d.getDiagnosis());

        // تفعيل الأزرار بعد التحميل
        btnSave.setEnabled(true);
        btnShare.setEnabled(true);
        btnPdf.setEnabled(true);
    }

    private void setupButtonListeners() {
        // زر الحفظ
        btnSave.setOnClickListener(v -> {
            SharedPreferences prefs =
                    PreferenceManager.getDefaultSharedPreferences(this);
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

        // زر المشاركة كنص
        btnShare.setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            sb.append("Code: ").append(currentCode.getCode()).append("\n")
                    .append("Title: ").append(currentCode.getTitle()).append("\n\n")
                    .append("Description:\n").append(currentCode.getDescription()).append("\n\n")
                    .append("Symptoms:\n").append(currentCode.getSymptoms()).append("\n\n")
                    .append("Causes:\n").append(currentCode.getCauses()).append("\n\n")
                    .append("Solutions:\n").append(currentCode.getSolutions()).append("\n\n")
                    .append("Severity: ").append(currentCode.getSeverity()).append("\n\n")
                    .append("Diagnosis:\n").append(currentCode.getDiagnosis());

            // إضافة رابط الصورة إذا كانت موجودة
            if (currentCode.getImage() != null && !currentCode.getImage().isEmpty()) {
                String imageUrl = ApiClient.IMAGE_BASE_URL + currentCode.getImage();
                sb.append("\n\n Image:\n").append(imageUrl);
            }

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, currentCode.getCode());
            shareIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());

            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_code)));
        });


        // زر توليد وفتح PDF
        btnPdf.setOnClickListener(v -> {
            try {
                String fileName = "OBD_" + currentCode.getCode() + ".pdf";
                File mainDir = new File(getExternalFilesDir(null), "OBD Codes/pdf");
                if (!mainDir.exists()) mainDir.mkdirs();
                File file = new File(mainDir, fileName);

                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                // إدراج صورة الكود إن وجدت
                if (currentCode.getImage() != null && !currentCode.getImage().isEmpty()) {
                    String imageUrl = ApiClient.IMAGE_BASE_URL + currentCode.getImage();
                    try {
                        Image img = Image.getInstance(new java.net.URL(imageUrl));
                        img.scaleToFit(500, 300); // تغيير الحجم حسب الحاجة
                        img.setAlignment(Image.ALIGN_CENTER);
                        document.add(img);
                        document.add(new Paragraph(" ")); // مسافة بعد الصورة
                    } catch (Exception e) {
                        e.printStackTrace(); // في حال فشل تحميل الصورة
                    }
                }

                document.add(new Paragraph("Code: " + currentCode.getCode()));
                document.add(new Paragraph("Title: " + currentCode.getTitle()));
                document.add(new Paragraph(""));
                document.add(new Paragraph("Description:\n" + currentCode.getDescription()));
                document.add(new Paragraph("Symptoms:\n" + currentCode.getSymptoms()));
                document.add(new Paragraph("Causes:\n" + currentCode.getCauses()));
                document.add(new Paragraph("Solutions:\n" + currentCode.getSolutions()));
                document.add(new Paragraph("Severity: " + currentCode.getSeverity()));
                document.add(new Paragraph("Diagnosis:\n" + currentCode.getDiagnosis()));
                document.close();

                Toast.makeText(this, "تم حفظ التقرير بنجاح", Toast.LENGTH_SHORT).show();

                Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
                Intent openIntent = new Intent(Intent.ACTION_VIEW);
                openIntent.setDataAndType(uri, "application/pdf");
                openIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(openIntent, "فتح التقرير باستخدام..."));

            } catch (Exception e) {
                Toast.makeText(this, "فشل في توليد التقرير", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });


    }
}
