package com.proapp.obdcodes.ui.subscription;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.databinding.ActivitySubscriptionDetailsBinding;
import com.proapp.obdcodes.network.model.Subscription;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.utils.BindingAdapters;
import com.proapp.obdcodes.viewmodel.UserViewModel;

import java.util.Locale;

public class SubscriptionDetailsActivity extends BaseActivity {
    private ActivitySubscriptionDetailsBinding binding;
    private UserViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_subscription_details);
        binding = ActivitySubscriptionDetailsBinding.bind(findViewById(R.id.rootLayout));

        vm = new ViewModelProvider(this).get(UserViewModel.class);
        loadDetails();
    }

    private void loadDetails() {
        vm.getUserProfile().observe(this, user -> {
            if (user == null) return;

            Subscription sub = user.getSubscription();
            if (sub != null) {
                binding.tvName.setText(sub.getName());
                binding.tvPrice.setText(String.format(Locale.getDefault(),
                        "%s USD", sub.getPrice()));
                binding.tvDuration.setText(String.format(Locale.getDefault(),
                        "%d days", sub.getDurationDays()));
            } else {
                binding.tvName.setText(user.getCurrentPlan() != null
                        ? user.getCurrentPlan()
                        : getString(R.string.free));
                binding.tvPrice.setText("-");
                binding.tvDuration.setText("-");
            }

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

            binding.btnRenew.setOnClickListener(v -> {
                String url = "https://obdcode.xyz/page/subscription/";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            });
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
