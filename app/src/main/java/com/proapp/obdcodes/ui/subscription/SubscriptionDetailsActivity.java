package com.proapp.obdcodes.ui.subscription;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.databinding.ActivitySubscriptionDetailsBinding;
import com.proapp.obdcodes.network.model.Subscription;
import com.proapp.obdcodes.utils.BindingAdapters;
import com.proapp.obdcodes.viewmodel.UserViewModel;

import java.util.Locale;

public class SubscriptionDetailsActivity extends AppCompatActivity {
    private ActivitySubscriptionDetailsBinding binding;
    private UserViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscription_details);
        setSupportActionBar(binding.toolbar);
        // إظهار زر الرجوع في الـ Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        vm = new ViewModelProvider(this).get(UserViewModel.class);
        loadDetails();
    }

    private void loadDetails() {
        vm.getUserProfile().observe(this, user -> {
            if (user == null) return;

            Subscription sub = user.getSubscription();
            if (sub != null) {
                // إذا جاءت Subscription من السيرفر
                binding.tvName.setText(sub.getName());
                binding.tvPrice.setText(String.format(Locale.getDefault(),
                        "%s USD", sub.getPrice()));
                binding.tvDuration.setText(String.format(Locale.getDefault(),
                        "%d days", sub.getDurationDays()));
            } else {
                //Fallback: عرض اسم الخطة الحالية فقط
                binding.tvName.setText(user.getCurrentPlan() != null
                        ? user.getCurrentPlan()
                        : getString(R.string.free));
                binding.tvPrice.setText("-");
                binding.tvDuration.setText("-");
            }

            // تواريخ الاشتراك
            binding.tvStart.setText(
                    BindingAdapters.formatDateWithPrefix(
                            this,
                            user.getPlanStartDate(),
                            R.string.plan_start
                    )
            );
            binding.tvRenewal.setText(
                    BindingAdapters.formatDateWithPrefix(
                            this,
                            user.getPlanRenewalDate(),
                            R.string.plan_renewal
                    )
            );

            // زرّ التجديد
            binding.btnRenew.setOnClickListener(v -> {
                String url = "https://obdcode.xyz/page/subscription/";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            });
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // التعامل مع زر الرجوع في الـ Toolbar
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
