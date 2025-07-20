package com.proapp.obdcodes.ui.plans;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.model.Plan;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.viewmodel.PlansViewModel;

import java.util.List;

public class PlansActivity extends BaseActivity {

    private PlansViewModel viewModel;
    private RecyclerView rvPlans;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_plans);

        rvPlans     = findViewById(R.id.rvPlans);
        progressBar = findViewById(R.id.progressBar);
        rvPlans.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(PlansViewModel.class);

        // مراقبة حالة التحميل
        viewModel.getLoading().observe(this, loading ->
                progressBar.setVisibility(loading ? View.VISIBLE : View.GONE)
        );

        // مراقبة الأخطاء
        viewModel.getError().observe(this, this::showError);

        // مراقبة نتيجة الاشتراك
        viewModel.getSubscribeResult().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "✅ تم الاشتراك بنجاح", Toast.LENGTH_SHORT).show();
                navigateToSubscriptionStatus();
            } else {
                Toast.makeText(this, "❌ فشل في الاشتراك", Toast.LENGTH_SHORT).show();
            }
        });

        // مراقبة نتيجة التفعيل
        viewModel.getActivateResult().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "✅ تم التفعيل بنجاح", Toast.LENGTH_SHORT).show();
                navigateToSubscriptionStatus();
            } else {
                Toast.makeText(this, "❌ كود غير صالح أو منتهي", Toast.LENGTH_SHORT).show();
            }
        });

        // مراقبة الباقات
        viewModel.getPlans().observe(this, this::showPlans);
        viewModel.loadPlans();
    }

    private void showPlans(List<Plan> plans) {
        if (plans == null || plans.isEmpty()) {
            Toast.makeText(this, R.string.no_plans, Toast.LENGTH_SHORT).show();
            return;
        }

        PlanRecyclerAdapter adapter = new PlanRecyclerAdapter(
                this, plans,
                new PlanRecyclerAdapter.OnPlanClickListener() {
                    @Override
                    public void onActivateCodeClick(@NonNull Plan plan) {
                        showActivateDialog(plan);
                    }
                    @Override
                    public void onBuyWithGoogleClick(@NonNull Plan plan) {
                        viewModel.subscribeWithToken(
                                plan.getId(),
                                plan.getGoogleProductId()
                        );
                    }
                }
        );
        rvPlans.setAdapter(adapter);
    }

    private void showActivateDialog(Plan plan) {
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_activate_code, null);
        EditText etCode = dialogView.findViewById(R.id.etActivationCode);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dialog.show();

        // زر التفعيل من داخل XML
        dialogView.findViewById(R.id.btnSubmitCode).setOnClickListener(v -> {
            String code = etCode.getText().toString().trim();
            if (code.isEmpty()) {
                Toast.makeText(this, R.string.enter_activation_code, Toast.LENGTH_SHORT).show();
                return;
            }
            dialog.dismiss();
            viewModel.activateByCode(plan.getId(), code);
        });
    }

    private void navigateToSubscriptionStatus() {
        startActivity(new Intent(this, SubscriptionStatusActivity.class));
        finish();
    }

    private void showError(String message) {
        if (message != null && !message.isEmpty()) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }
}
