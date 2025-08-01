package com.eyadalalimi.car.obd2.ui.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.databinding.ActivityAccountBinding;
import com.eyadalalimi.car.obd2.network.ApiClient;
import com.eyadalalimi.car.obd2.ui.auth.AuthActivity;
import com.eyadalalimi.car.obd2.ui.base.BaseActivity;
import com.eyadalalimi.car.obd2.ui.cars.CarListActivity;
import com.eyadalalimi.car.obd2.ui.saved.SavedCodesActivity;
import com.eyadalalimi.car.obd2.ui.plans.SubscriptionStatusActivity;
import com.eyadalalimi.car.obd2.viewmodel.SubscriptionViewModel;
import com.eyadalalimi.car.obd2.utils.BindingAdapters;
import com.eyadalalimi.car.obd2.viewmodel.UserViewModel;

public class AccountActivity extends BaseActivity {
    private static final String IMAGE_BASE_URL = "https://obdcodehub.com/storage/";
    private static final int RC_EDIT_PROFILE = 3001;
    private ActivityAccountBinding binding;
    private UserViewModel vm;
    private SubscriptionViewModel subscriptionVM;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1) حقن تخطيط الفرعي داخل الحاوية في activity_base.xml
        setActivityLayout(R.layout.activity_account);
        // 2) ربط DataBinding على root معرّف root_account_layout
        binding = DataBindingUtil.bind(findViewById(R.id.root_account_layout));





        // 5) تهيئة ViewModel للاشتراك
        subscriptionVM = new ViewModelProvider(this)
                .get(SubscriptionViewModel.class);
        subscriptionVM.getCurrentSubscription().observe(this, sub -> {
            if (sub != null) {
                boolean isPaid = "active".equalsIgnoreCase(sub.getStatus());

                // وضع المستخدم
                binding.tvUserMode.setText(
                        getString(R.string.user_mode) + ": " +
                                (isPaid ? getString(R.string.paid) : getString(R.string.free))
                );
                // معلومات الاشتراك
                binding.tvCurrentPackage.setText(
                        getString(R.string.package_prefix) + ": " + sub.getName()
                );
                binding.tvCurrentPlan.setText(
                        getString(R.string.status_prefix) + ": " +
                                (isPaid ? getString(R.string.verified) : getString(R.string.not_verified))
                );
                binding.tvPlanStartDate.setText(
                        sub.getStartAt() != null
                                ? BindingAdapters.formatDateWithPrefix(this, sub.getStartAt(), R.string.plan_start)
                                : "-"
                );
                binding.tvPlanRenewalDate.setText(
                        sub.getEndAt() != null
                                ? BindingAdapters.formatDateWithPrefix(this, sub.getEndAt(), R.string.plan_renewal)
                                : "-"
                );
            }
        });
        subscriptionVM.refresh();

        // 6) تهيئة ViewModel المستخدم
        vm = new ViewModelProvider(this).get(UserViewModel.class);

        // 7) ربط المستمعين
        setupListeners();

        // 8) تحميل بيانات الحساب
        loadProfile();
    }

    @Override
    protected boolean shouldShowBottomNav() {
        // نخفي القائمة السفلية تماماً
        return true;
    }

    private void setupListeners() {
        // تعديل الملف الشخصي
        binding.btnEdit.setOnClickListener(v ->
                startActivityForResult(
                        new Intent(this, EditProfileActivity.class),
                        RC_EDIT_PROFILE
                )
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
                        startActivity(new Intent(this, AuthActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        );
                    } else {
                        Toast.makeText(this, R.string.err_logout, Toast.LENGTH_SHORT).show();
                    }
                })
        );

        // حذف الحساب
        binding.btnDelete.setOnClickListener(v -> {
            View dialogView = getLayoutInflater().inflate(R.layout.custom_confirm_delete_dialog, null);

            androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setView(dialogView)
                    .setCancelable(false)
                    .create();

            // ربط الأزرار
            Button btnCancel = dialogView.findViewById(R.id.btnCancel);
            Button btnDelete = dialogView.findViewById(R.id.btnDelete);

            btnCancel.setOnClickListener(x -> dialog.dismiss());

            btnDelete.setOnClickListener(x -> {
                dialog.dismiss();
                vm.deleteAccount().observe(this, success -> {
                    if (Boolean.TRUE.equals(success)) {
                        SharedPreferences prefs = PreferenceManager
                                .getDefaultSharedPreferences(this);
                        prefs.edit()
                                .remove("auth_token")
                                .putBoolean("is_logged_in", false)
                                .apply();

                        ApiClient.reset();
                        startActivity(new Intent(this, AuthActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        );
                    } else {
                        Toast.makeText(this, R.string.err_delete_account, Toast.LENGTH_SHORT).show();
                    }
                });
            });

            dialog.show();
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_EDIT_PROFILE && resultCode == RESULT_OK) {
            // إعادة تحميل البيانات عند العودة من شاشة التحرير
            loadProfile();
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadProfile() {
        vm.getUserProfile().observe(this, user -> {
            if (user == null) {
                Toast.makeText(this, R.string.err_fetch_profile, Toast.LENGTH_SHORT).show();
                return;
            }

            // صورة المستخدم
            String imgPath = user.getProfileImage();
            if (imgPath != null && !imgPath.isEmpty()) {
                String fullUrl = imgPath.startsWith("http") ? imgPath : IMAGE_BASE_URL + imgPath;
                Glide.with(this)
                        .load(fullUrl)
                        .circleCrop()
                        .placeholder(R.drawable.profile)
                        .error(R.drawable.profile_sample)
                        .into(binding.imgProfile);
            } else {
                binding.imgProfile.setImageResource(R.drawable.profile);
            }

            // البيانات الأساسية
            binding.tvUsername.setText(getString(R.string.user_name) + ": " + user.getUsername());
            binding.tvEmail.setText(getString(R.string.email) + ": " + user.getEmail());
            binding.tvJobTitle.setText(
                    getString(R.string.job_title) + ": " +
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
                    getString(R.string.saved_codes) + " " + user.getSavedCodesCount()
            );
        });
    }
}
