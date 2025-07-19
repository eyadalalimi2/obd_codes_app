package com.proapp.obdcodes.ui.analytics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.model.ObdCode;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.util.SubscriptionUtils;
import com.proapp.obdcodes.viewmodel.TrendingViewModel;

public class TrendingCodesActivity extends BaseActivity {

    private TrendingAdapter adapter;
    private TrendingViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SubscriptionUtils.checkFeatureAccess(
                this,
                "TRENDING_CODES_ANALYTICS",
                this::initTrendingUI
        );
    }

    private void initTrendingUI() {
        runOnUiThread(() -> {
            setActivityLayout(R.layout.activity_trending_codes);
            setTitle(getString(R.string.trending_codes));

            RecyclerView rv = findViewById(R.id.rvTrendingCodes);
            rv.setLayoutManager(new LinearLayoutManager(this));

            // تهيئة adapter بدون لامبدا في الـ Constructor
            adapter = new TrendingAdapter();
            // ضبط listener بعدها
            adapter.setOnItemClickListener(code -> showDetailsDialog(code));
            rv.setAdapter(adapter);

            viewModel = new ViewModelProvider(
                    this,
                    new ViewModelProvider.AndroidViewModelFactory(getApplication())
            ).get(TrendingViewModel.class);

            viewModel.getTrendingCodes().observe(this, codes -> {
                adapter.setItems(codes);
            });
        });
    }


    private void showDetailsDialog(ObdCode code) {
        View view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_code_details, null);

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

        new MaterialAlertDialogBuilder(this)
                .setView(view)
                .setPositiveButton(R.string.close, null)
                .show();
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return true;
    }
}
