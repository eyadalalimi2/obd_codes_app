package com.proapp.obdcodes.ui.account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.databinding.ActivityAccountBinding;
import com.proapp.obdcodes.network.ApiClient;
import com.proapp.obdcodes.network.model.User;
import com.proapp.obdcodes.ui.auth.AuthActivity;
import com.proapp.obdcodes.ui.auth.RegisterActivity;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.ui.cars.CarListActivity;
import com.proapp.obdcodes.ui.saved.SavedCodesActivity;
import com.proapp.obdcodes.ui.subscription.SubscriptionStatusActivity;
import com.proapp.obdcodes.ui.subscription.SubscriptionViewModel;
import com.proapp.obdcodes.utils.BindingAdapters;
import com.proapp.obdcodes.viewmodel.UserViewModel;

public class AccountActivity extends BaseActivity {
    private static final String IMAGE_BASE_URL = "https://obdcode.xyz/storage/";

    private ActivityAccountBinding binding;
    private UserViewModel vm;
    private SubscriptionViewModel subscriptionVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.my_account);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }


        // 1)هيّئ ViewModel الاشتراك ومراقبة البيانات قبل ربط أي مستمعين
        subscriptionVM = new ViewModelProvider(this).get(SubscriptionViewModel.class);
        subscriptionVM.getCurrentSubscription().observe(this, sub -> {
            if (sub != null) {
                boolean isPaid = "active".equalsIgnoreCase(sub.getStatus());

                // وضع المستخدم (مجاني/مدفوع)
                binding.tvUserMode.setText(
                        getString(R.string.user_mode) + ": " +
                                (isPaid ? getString(R.string.paid) : getString(R.string.free))
                );
                // اسم الباقة
                binding.tvCurrentPackage.setText(
                        getString(R.string.package_prefix) + ": " + sub.getName()
                );
                // حالة الاشتراك
                binding.tvCurrentPlan.setText(
                        getString(R.string.status_prefix) + ": " +
                                (isPaid ? getString(R.string.active_ar) : getString(R.string.inactive_ar))
                );
                // تاريخ البدء
                binding.tvPlanStartDate.setText(
                        sub.getStartAt() != null
                                ? BindingAdapters.formatDateWithPrefix(this, sub.getStartAt(), R.string.plan_start)
                                : "-"
                );
                // تاريخ الانتهاء
                binding.tvPlanRenewalDate.setText(
                        sub.getEndAt() != null
                                ? BindingAdapters.formatDateWithPrefix(this, sub.getEndAt(), R.string.plan_renewal)
                                : "-"
                );
            }
        });
        subscriptionVM.refresh();

        // 2)هيّئ ViewModel المستخدم
        vm = new ViewModelProvider(this).get(UserViewModel.class);

        // 3) اربط كل Listeners
        setupListeners();

        // 4) حمّل بيانات الحساب
        loadProfile();
    }



    private void setupListeners() {
        // تعديل الملف الشخصي
        binding.btnEdit.setOnClickListener(v ->
                startActivity(new Intent(this, EditProfileActivity.class))
        );

        // إدارة الاشتراك
        binding.llManageSubscription.setOnClickListener(v ->
                startActivity(new Intent(this, SubscriptionStatusActivity.class))
        );

        // عرض قائمة السيارات
        binding.llPackageCard.setOnClickListener(v ->
                startActivity(new Intent(this, CarListActivity.class))
        );

        // عرض الأكواد المحفوظة
        binding.llSavedCodes.setOnClickListener(v ->
                startActivity(new Intent(this, SavedCodesActivity.class))
        );

        // تسجيل الخروج
        binding.btnLogout.setOnClickListener(v ->
                vm.logout().observe(this, success -> {
                    if (Boolean.TRUE.equals(success)) {
                        SharedPreferences prefs = PreferenceManager
                                .getDefaultSharedPreferences(this);
                        prefs.edit()
                                .remove("auth_token")
                                .putBoolean("is_logged_in", false)
                                .apply();

                        ApiClient.reset();
                        Intent intent = new Intent(this, AuthActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, R.string.err_logout, Toast.LENGTH_SHORT).show();
                    }
                })
        );

        // حذف الحساب
        binding.btnDelete.setOnClickListener(v ->
                vm.deleteAccount().observe(this, success -> {
                    if (Boolean.TRUE.equals(success)) {
                        SharedPreferences prefs = PreferenceManager
                                .getDefaultSharedPreferences(this);
                        prefs.edit()
                                .remove("auth_token")
                                .putBoolean("is_logged_in", false)
                                .apply();

                        ApiClient.reset();
                        Intent intent = new Intent(this, RegisterActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, R.string.err_delete_account, Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }

    private void loadProfile() {
        vm.getUserProfile().observe(this, user -> {
            if (user == null) {
                Toast.makeText(this, R.string.err_fetch_profile, Toast.LENGTH_SHORT).show();
                return;
            }

            // صورة وبيانات أساسية
            String imgPath = user.getProfileImage();
            if (imgPath != null && !imgPath.isEmpty()) {
                String fullUrl = imgPath.startsWith("http") ? imgPath : IMAGE_BASE_URL + imgPath;
                Glide.with(this)
                        .load(fullUrl)
                        .circleCrop()
                        .placeholder(R.drawable.profile_sample)
                        .error(R.drawable.profile_sample)
                        .into(binding.imgProfile);
            } else {
                binding.imgProfile.setImageResource(R.drawable.profile_sample);
            }

            binding.tvUsername.setText(getString(R.string.user_name) + ": " + user.getUsername());
            binding.tvEmail.setText(getString(R.string.email) + ": " + user.getEmail());
            binding.tvJobTitle.setText(
                    getString(R.string.job_title_prefix) + ": " +
                            (user.getJobTitle() != null ? user.getJobTitle() : getString(R.string.undefined))
            );

            // ثابت "سياراتي"
            binding.tvCar.setText(R.string.my_cars);

            binding.tvPhone.setText(
                    getString(R.string.phone) + " " +
                            (user.getPhone() != null ? user.getPhone() : "-")
            );
            binding.tvCreatedAt.setText(
                    BindingAdapters.formatDateWithPrefix(this, user.getCreatedAt(), R.string.created_on)
            );

            // عدد الأكواد المحفوظة
            binding.tvSavedCodesCount.setText(
                    getString(R.string.saved_codes_prefix) + " " + user.getSavedCodesCount()
            );
        });
    }
    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }
}
