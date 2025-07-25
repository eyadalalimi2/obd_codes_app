package com.eyadalalimi.car.obd2.ui.plans;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.network.model.Subscription;
import com.eyadalalimi.car.obd2.ui.base.BaseActivity;
import com.eyadalalimi.car.obd2.util.FeatureMapper;
import com.eyadalalimi.car.obd2.viewmodel.SubscriptionViewModel;

public class SubscriptionStatusActivity extends BaseActivity {

    private SubscriptionViewModel viewModel;
    private TextView tvPlanName, tvSubscriptionStatus, tvStartDate, tvExpiresAt, tvDaysLeft;
    private Button btnRenew, btnCancel;
    private ProgressBar progressBar;
    private LinearLayout llCurrentFeatures;
    private TextView tvFeaturesTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_subscription_status);

        tvPlanName           = findViewById(R.id.tvPlanName);
        tvSubscriptionStatus = findViewById(R.id.tvSubscriptionStatus);
        tvStartDate          = findViewById(R.id.tvStartDate);
        tvExpiresAt          = findViewById(R.id.tvExpiresAt);
        tvDaysLeft           = findViewById(R.id.tvDaysLeft);
        btnRenew             = findViewById(R.id.btnRenew);
        btnCancel            = findViewById(R.id.btnCancel);
        progressBar          = findViewById(R.id.progressBar);
        llCurrentFeatures    = findViewById(R.id.llCurrentFeatures);
        tvFeaturesTitle = findViewById(R.id.tvFeaturesTitle);

        viewModel = new ViewModelProvider(this)
                .get(SubscriptionViewModel.class);

        viewModel.getLoading().observe(this, loading ->
                progressBar.setVisibility(loading ? View.VISIBLE : View.GONE)
        );

        viewModel.getError().observe(this, msg -> {
            if (msg != null) showToast(msg);
        });

        viewModel.getCurrentSubscription().observe(this, this::bindSubscription);

        viewModel.getRenewResult().observe(this, success -> {
            showToast(success ? "✅ تم التجديد بنجاح" : "❌ فشل في التجديد");
            if (success) viewModel.refresh();
        });

        viewModel.getCancelResult().observe(this, success -> {
            showToast(success ? "✅ تم الإلغاء بنجاح" : "❌ فشل في الإلغاء");
            if (success) viewModel.refresh();
        });

        btnRenew.setOnClickListener(v -> showRenewDialog());

        btnCancel.setOnClickListener(v -> showCancelDialog());


        viewModel.refresh();
    }

    private void showRenewDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_renew_subscription, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        Button btnYes = dialogView.findViewById(R.id.btnRenewYes);
        Button btnNo = dialogView.findViewById(R.id.btnRenewNo);

        btnYes.setOnClickListener(v -> {
            dialog.dismiss();
            viewModel.renewSubscription();
        });

        btnNo.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


    private void showCancelDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_cancel_subscription, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        Button btnYes = dialogView.findViewById(R.id.btnCancelYes);
        Button btnNo = dialogView.findViewById(R.id.btnCancelNo);

        btnYes.setOnClickListener(v -> {
            dialog.dismiss();
            viewModel.cancelSubscription();
        });

        btnNo.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void bindSubscription(Subscription sub) {
        if (sub != null && sub.getName() != null) {
            // عرض تفاصيل الباقة الحالية
            tvPlanName.setText("الباقة: " + sub.getName());

            boolean active = sub.getDaysLeft() > 0;
            tvSubscriptionStatus.setText("الحالة: " + (active ? "نشط" : "منتهي"));
            tvSubscriptionStatus.setTextColor(ContextCompat.getColor(
                    this, active ? R.color.success : R.color.error
            ));

            tvStartDate.setText(getString(R.string.subscription_start) + " " + sub.getStartDateFormatted());
            tvExpiresAt.setText(getString(R.string.subscription_end) + " " + sub.getEndDateFormatted());
            tvDaysLeft.setText(getString(R.string.days_left) + " " + sub.getDaysLeft() + " يوم");
            tvFeaturesTitle.setVisibility(View.VISIBLE);
            llCurrentFeatures.setVisibility(View.VISIBLE);

            // عرض الميزات
            llCurrentFeatures.removeAllViews();
            if (sub.getFeatures() != null) {
                for (String key : sub.getFeatures()) {
                    View row = getLayoutInflater().inflate(R.layout.item_feature, llCurrentFeatures, false);
                    ((TextView) row.findViewById(R.id.tvFeature))
                            .setText(FeatureMapper.toReadable(key));
                    llCurrentFeatures.addView(row);
                }
            }

            // إعداد الأزرار
            btnRenew.setText("تجديد الاشتراك");
            btnRenew.setVisibility(View.VISIBLE);
            btnRenew.setOnClickListener(v -> showRenewDialog());

            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(v -> showCancelDialog());

        } else {
            // لا يوجد اشتراك حالي
            tvPlanName.setText("لا توجد باقة حالياً");
            tvSubscriptionStatus.setText("");
            tvStartDate.setText("");
            tvExpiresAt.setText("");
            tvDaysLeft.setText("");
            llCurrentFeatures.removeAllViews();
            tvFeaturesTitle.setVisibility(View.GONE);
            llCurrentFeatures.setVisibility(View.GONE);
            btnRenew.setText("الاشتراك في باقة");
            btnRenew.setVisibility(View.VISIBLE);
            btnRenew.setOnClickListener(v -> {
                startActivity(new Intent(this, PlansActivity.class));
                finish();
            });

            btnCancel.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.refresh();
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }
}
