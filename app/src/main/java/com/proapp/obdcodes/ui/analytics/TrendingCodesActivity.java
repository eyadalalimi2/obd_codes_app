package com.proapp.obdcodes.ui.analytics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.model.ObdCode;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.viewmodel.TrendingViewModel;

import java.util.List;

public class TrendingCodesActivity extends BaseActivity {
    private TrendingAdapter adapter;
    private TrendingViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_trending_codes);
        setTitle(getString(R.string.trending_codes));

        // إرفاق RecyclerView
        androidx.recyclerview.widget.RecyclerView rv = findViewById(R.id.rvTrendingCodes);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TrendingAdapter();
        rv.setAdapter(adapter);

        // جلب البيانات
        viewModel = new androidx.lifecycle.ViewModelProvider(this,
                androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
        ).get(TrendingViewModel.class);

        viewModel.getTrendingCodes().observe(this, codes -> {
            adapter.setItems(codes);
            adapter.setOnItemClickListener(this::showDetailsDialog);
        });
    }

    /**
     * هذه الدالة تستدعى عند الضغط على أي عنصر في القائمة
     */
    private void showDetailsDialog(ObdCode code) {
        // اInflate واجهة الحوار
        View view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_code_details, null);

        // ربط Views وتعبئتها
        ((TextView) view.findViewById(R.id.tvDialogCode))
                .setText(code.getCode());
        ((TextView) view.findViewById(R.id.tvDialogTitle))
                .setText(code.getTitle());
        ((TextView) view.findViewById(R.id.tvDialogDescription))
                .setText(code.getDescription());
        ((TextView) view.findViewById(R.id.tvDialogSymptoms))
                .setText(code.getSymptoms());
        ((TextView) view.findViewById(R.id.tvDialogCauses))
                .setText(code.getCauses());
        ((TextView) view.findViewById(R.id.tvDialogSolutions))
                .setText(code.getSolutions());
        ((TextView) view.findViewById(R.id.tvDialogSeverity))
                .setText(code.getSeverity());
        ((TextView) view.findViewById(R.id.tvDialogDiagnosis))
                .setText(code.getDiagnosis());

        // عرض الحوار
        new MaterialAlertDialogBuilder(this)
                .setView(view)
                .setPositiveButton(R.string.close, null)
                .show();
    }
}
