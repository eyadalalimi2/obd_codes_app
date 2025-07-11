package com.proapp.obdcodes.ui.subscription;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.model.Subscription;
import com.proapp.obdcodes.ui.base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SubscriptionStatusActivity extends BaseActivity {

    private SubscriptionViewModel viewModel;

    private TextView tvPlanName, tvSubscriptionStatus, tvStartDate,
            tvExpiresAt, tvDaysLeft, tvFeatures;
    private Button btnRenew, btnCancel;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_subscription_status);

        // ======= ربط الـ Views =======
        tvPlanName           = findViewById(R.id.tvPlanName);
        tvSubscriptionStatus = findViewById(R.id.tvSubscriptionStatus);
        tvStartDate          = findViewById(R.id.tvStartDate);
        tvExpiresAt          = findViewById(R.id.tvExpiresAt);
        tvDaysLeft           = findViewById(R.id.tvDaysLeft);
        tvFeatures           = findViewById(R.id.tvFeatures);
        btnRenew             = findViewById(R.id.btnRenew);
        btnCancel            = findViewById(R.id.btnCancel);
        progressBar          = findViewById(R.id.progressBar);

        // ======= إنشاء الـ ViewModel =======
        viewModel = new ViewModelProvider(this)
                .get(SubscriptionViewModel.class);

        // ======= مراقبة LiveData من الـ ViewModel =======
        viewModel.getCurrentSubscription().observe(this, this::updateUI);

        viewModel.getLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getError().observe(this, errorMsg -> {
            if (errorMsg != null) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getRenewResult().observe(this, success -> {
            Toast.makeText(
                    this,
                    success ? "✅ تم التجديد بنجاح" : "❌ فشل في التجديد",
                    Toast.LENGTH_SHORT
            ).show();
            if (success) viewModel.refresh();
        });

        viewModel.getCancelResult().observe(this, success -> {
            Toast.makeText(
                    this,
                    success ? "✅ تم الإلغاء بنجاح" : "❌ فشل في الإلغاء",
                    Toast.LENGTH_SHORT
            ).show();
            if (success) viewModel.refresh();
        });

        // ======= أحداث الأزرار =======
        btnRenew.setOnClickListener(v -> viewModel.renewSubscription());
        btnCancel.setOnClickListener(v -> viewModel.cancelSubscription());

        // ======= جلب بيانات الاشتراك لأول مرة =======
        viewModel.refresh();
    }

    /**
     * يحدث واجهة المستخدم بناءً على حالة الاشتراك.
     */
    private void updateUI(Subscription subscription) {
        if (subscription != null) {
            tvPlanName.setText("الباقة: " + subscription.getName());
            tvSubscriptionStatus.setText("الحالة: نشط");
            tvStartDate.setText("التفعيل: " + subscription.getStartDate());
            tvExpiresAt.setText("الانتهاء: " + subscription.getExpiresAt());

            int daysLeft = calculateDaysLeft(subscription.getExpiresAt());
            tvDaysLeft.setText("المتبقي: " + daysLeft + " يوم");

            String features = subscription.getFeaturesText();
            tvFeatures.setText(
                    features.isEmpty() ? "لا توجد مميزات محددة" : features
            );

            // عرض/إخفاء الأزرار
            btnRenew.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
        } else {
            tvPlanName.setText("لا يوجد اشتراك نشط");
            tvSubscriptionStatus.setText("");
            tvStartDate.setText("");
            tvExpiresAt.setText("");
            tvDaysLeft.setText("");
            tvFeatures.setText("");

            btnRenew.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
        }
    }

    /**
     * يحسب عدد الأيام المتبقية حتى تاريخ الانتهاء (yyyy-MM-dd).
     */
    private int calculateDaysLeft(String expiresAt) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd", Locale.getDefault()
            );
            long endMs = sdf.parse(expiresAt).getTime();
            long diff  = endMs - System.currentTimeMillis();
            return (int) (diff / (1000L * 60 * 60 * 24));
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }
}
