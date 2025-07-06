package com.proapp.obdcodes.ui.code_details;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.ApiClient;
import com.proapp.obdcodes.network.model.ObdCode;
import com.proapp.obdcodes.viewmodel.CodeDetailViewModel;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.viewmodel.UserViewModel;

public class CodeDetailsActivity extends BaseActivity {

    private TextView tvCode, tvTitle, tvType, tvBrandId,
            tvDescription, tvSymptoms,
            tvCauses, tvSolutions,
            tvSeverity, tvDiagnosis;
    private ImageView ivImage;
    private UserViewModel vm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // هذا سيستدعي BaseActivity.setContentView ويحقن القالب العام
        setContentView(R.layout.activity_code_details);

        // ربط عناصر الواجهة
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

        String code = getIntent().getStringExtra("CODE");

        CodeDetailViewModel viewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication()))
                .get(CodeDetailViewModel.class);

        viewModel.loadCodeDetail(code);
        viewModel.getCodeDetail().observe(this, this::bindData);
    }

    private void bindData(@Nullable ObdCode d) {
        if (d == null) {
            tvCode.setText("خطأ في جلب البيانات");
            return;
        }

        tvCode.setText(d.getCode());
        tvTitle.setText(d.getTitle());

        // تحميل الصورة (إن وجدت)
        if (d.getImage() != null && !d.getImage().isEmpty()) {
            ivImage.setVisibility(View.VISIBLE);
            String fullUrl = ApiClient.IMAGE_BASE_URL + d.getImage();
            Glide.with(this)
                    .load(fullUrl)
                    .placeholder(R.drawable.profile)
                    .into(ivImage);
        } else {
            ivImage.setVisibility(View.GONE);
        }

        tvType.setText(d.getType());
        tvBrandId.setText(d.getBrandId()!=null
                ? String.valueOf(d.getBrandId()) : "-");

        tvDescription.setText(d.getDescription());
        tvSymptoms.setText(d.getSymptoms());
        tvCauses.setText(d.getCauses());
        tvSolutions.setText(d.getSolutions());
        tvSeverity.setText(d.getSeverity());
        tvDiagnosis.setText(d.getDiagnosis());
    }
}
