// com.proapp.obdcodes.ui.plans/PlansActivity.java
package com.proapp.obdcodes.ui.plans;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.model.Plan;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.ui.subscription.SubscriptionStatusActivity;

import java.util.List;

public class PlansActivity extends BaseActivity {

    private PlansViewModel viewModel;
    private RecyclerView   rvPlans;
    private ProgressBar    progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_plans);

        // ربط الـ Views
        rvPlans      = findViewById(R.id.rvPlans);
        rvPlans.setLayoutManager(new LinearLayoutManager(this));
        progressBar  = findViewById(R.id.progressBar);

        // تهيئة الـ ViewModel
        viewModel = new ViewModelProvider(this).get(PlansViewModel.class);

        // مراقبة حالة التحميل
        viewModel.getLoading().observe(this, isLoading ->
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE)
        );

        // مراقبة الأخطاء
        viewModel.getError().observe(this, this::showError);

        // بعد نجاح الاشتراك عبر Google
        viewModel.getSubscribeResult().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "✅ تم الاشتراك بنجاح", Toast.LENGTH_SHORT).show();
                navigateToSubscriptionStatus();
            } else {
                Toast.makeText(this, "❌ فشل في الاشتراك", Toast.LENGTH_SHORT).show();
            }
        });

        // بعد نجاح التفعيل بالكود
        viewModel.getActivateResult().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "✅ تم التفعيل بنجاح", Toast.LENGTH_SHORT).show();
                navigateToSubscriptionStatus();
            } else {
                Toast.makeText(this, "❌ كود غير صالح أو منتهي", Toast.LENGTH_SHORT).show();
            }
        });

        // جلب الباقات
        viewModel.loadPlans();
        viewModel.getPlans().observe(this, this::showPlans);
    }

    private void showPlans(List<Plan> plans) {
        if (plans == null || plans.isEmpty()) {
            Toast.makeText(this, "لا توجد باقات حالياً", Toast.LENGTH_SHORT).show();
            return;
        }

        PlanRecyclerAdapter adapter = new PlanRecyclerAdapter(
                this,
                plans,
                new PlanRecyclerAdapter.OnPlanClickListener() {
                    @Override
                    public void onActivateCodeClick(Plan plan) {
                        showActivateDialog(plan);
                    }
                    @Override
                    public void onBuyWithGoogleClick(Plan plan) {
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

        dialogView.findViewById(R.id.btnSubmitCode).setOnClickListener(v -> {
            String code = etCode.getText().toString().trim();
            if (code.isEmpty()) {
                Toast.makeText(this, "الرجاء إدخال كود التفعيل", Toast.LENGTH_SHORT).show();
                return;
            }
            dialog.dismiss();
            viewModel.activateByCode(plan.getId(), code);
        });

        dialog.show();
    }

    private void navigateToSubscriptionStatus() {
        startActivity(new Intent(this, SubscriptionStatusActivity.class));
        finish();
    }

    private void showError(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }
}
