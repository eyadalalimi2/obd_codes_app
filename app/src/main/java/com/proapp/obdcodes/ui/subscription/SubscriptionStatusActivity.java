// com.proapp.obdcodes.ui.subscription/SubscriptionStatusActivity.java
package com.proapp.obdcodes.ui.subscription;

import android.content.Intent;
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
import com.proapp.obdcodes.ui.plans.PlansActivity;

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

        // ربط الـ Views
        tvPlanName           = findViewById(R.id.tvPlanName);
        tvSubscriptionStatus = findViewById(R.id.tvSubscriptionStatus);
        tvStartDate          = findViewById(R.id.tvStartDate);
        tvExpiresAt          = findViewById(R.id.tvExpiresAt);
        tvDaysLeft           = findViewById(R.id.tvDaysLeft);
        btnRenew             = findViewById(R.id.btnRenew);
        btnCancel            = findViewById(R.id.btnCancel);
        progressBar          = findViewById(R.id.progressBar);

        // إنشاء الـ ViewModel
        viewModel = new ViewModelProvider(this)
                .get(SubscriptionViewModel.class);

        // مراقبة بيانات الاشتراك
        viewModel.getCurrentSubscription().observe(this, this::bindSubscription);

        // مراقبة حالة التحميل
        viewModel.getLoading().observe(this, isLoading ->
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE)
        );

        // مراقبة الأخطاء
        viewModel.getError().observe(this, errorMsg -> {
            if (errorMsg != null) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });

        // مراقبة نتيجة التجديد
        viewModel.getRenewResult().observe(this, success -> {
            Toast.makeText(
                    this,
                    success ? "✅ تم التجديد بنجاح" : "❌ فشل في التجديد",
                    Toast.LENGTH_SHORT
            ).show();
            if (success) viewModel.refresh();
        });

        // مراقبة نتيجة الإلغاء
        viewModel.getCancelResult().observe(this, success -> {
            Toast.makeText(
                    this,
                    success ? "✅ تم الإلغاء بنجاح" : "❌ فشل في الإلغاء",
                    Toast.LENGTH_SHORT
            ).show();
            if (success) viewModel.refresh();
        });

        // أحداث الأزرار
        btnRenew.setOnClickListener(v -> viewModel.renewSubscription());
        btnCancel.setOnClickListener(v -> viewModel.cancelSubscription());

        // أول جلب للبيانات
        viewModel.refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.refresh();  // تأكد من تحديث البيانات عند العودة للصفحة
    }

    // ربط البيانات إلى الواجهة
    private void bindSubscription(Subscription sub) {
        if (sub != null && sub.getName() != null) {
            tvPlanName.setText("الباقة: " + sub.getName());
            tvSubscriptionStatus.setText("الحالة: " + (sub.getDaysLeft() > 0 ? "نشط" : "منتهي"));
            tvStartDate.setText("التفعيل: " + sub.getStartDateFormatted());
            tvExpiresAt.setText("الانتهاء: " + sub.getEndDateFormatted());
            tvDaysLeft.setText("المتبقي: " + sub.getDaysLeft() + " يوم");
            ;

            btnRenew.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
        } else {
            // لا اشتراك
            tvPlanName.setText("لا يوجد اشتراك نشط");
            tvSubscriptionStatus.setText("");
            tvStartDate.setText("");
            tvExpiresAt.setText("");
            tvDaysLeft.setText("");


            btnRenew.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
        }
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }
}
