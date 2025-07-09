package com.proapp.obdcodes.ui.account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.model.UpdateProfileRequest;
import com.proapp.obdcodes.network.model.User;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.viewmodel.UserViewModel;

public class EditProfileActivity extends BaseActivity {

    private EditText etUsername, etEmail, etPhone, etJobTitle;
    private TextView tvPackageNameValue, tvSubscriptionStatusValue, tvPlanStartDateValue, tvPlanEndDateValue;
    private Button btnSave, btnViewSubscriptionDetails, btnVerifyEmail;
    private UserViewModel vm;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_edit_profile);

        // ربط الحقول من الـ XML
        etUsername = findViewById(R.id.etUsername);
        etEmail    = findViewById(R.id.etEmail);
        etPhone    = findViewById(R.id.etPhone);
        etJobTitle = findViewById(R.id.etJobTitle);
        btnSave    = findViewById(R.id.btnSaveProfile);

        // حقول عرض الاشتراك
        tvPackageNameValue        = findViewById(R.id.tvPackageNameValue);
        tvSubscriptionStatusValue = findViewById(R.id.tvSubscriptionStatusValue);
        tvPlanStartDateValue      = findViewById(R.id.tvPlanStartDateValue);
        tvPlanEndDateValue        = findViewById(R.id.tvPlanEndDateValue);
        btnViewSubscriptionDetails= findViewById(R.id.btnViewSubscriptionDetails);

        // زر إعادة التحقق من البريد (يظهر فقط إذا لم يتم التحقق)
        btnVerifyEmail = findViewById(R.id.btnVerifyEmail);

        // تهيئة الـ ViewModel
        vm = new ViewModelProvider(this).get(UserViewModel.class);

        // جلب البيانات الحالية للمستخدم
        vm.getUserProfile().observe(this, user -> {
            if (user != null) {
                currentUser = user;
                etUsername.setText(user.getUsername());
                etEmail   .setText(user.getEmail());
                etPhone   .setText(user.getPhone());
                etJobTitle.setText(user.getJobTitle());

                // عرض بيانات الاشتراك
                tvPackageNameValue.setText(
                        user.getCurrentPlan() != null ? user.getCurrentPlan() : getString(R.string.not_set)
                );
                tvSubscriptionStatusValue.setText(
                        "paid".equals(user.getUserMode()) ? getString(R.string.paid) : getString(R.string.free)
                );
                tvPlanStartDateValue.setText(
                        user.getPlanStartDate() != null ? user.getPlanStartDate() : "-"
                );
                tvPlanEndDateValue.setText(
                        user.getPlanRenewalDate() != null ? user.getPlanRenewalDate() : "-"
                );

                // إظهار أو إخفاء زر التحقق من الإيميل
                if (user.getEmailVerifiedAt() == null) {
                    btnVerifyEmail.setVisibility(View.VISIBLE);
                } else {
                    btnVerifyEmail.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(this, R.string.err_fetch_profile, Toast.LENGTH_SHORT).show();
            }
        });

        // حفظ التعديلات عند الضغط على زر الحفظ
        btnSave.setOnClickListener(v -> {
            UpdateProfileRequest req = new UpdateProfileRequest(
                    etUsername.getText().toString().trim(),
                    etEmail   .getText().toString().trim(),
                    etPhone   .getText().toString().trim(),
                    etJobTitle.getText().toString().trim()
            );
            

            vm.updateUserProfile(req).observe(this, success -> {
                if (Boolean.TRUE.equals(success)) {
                    Toast.makeText(this, R.string.msg_profile_saved, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, R.string.err_save_profile, Toast.LENGTH_SHORT).show();
                }
            });
        });

        // زر عرض تفاصيل الاشتراك
        btnViewSubscriptionDetails.setOnClickListener(v -> {
            if (currentUser != null) {
                String url = "https://obdcode.xyz/users/" + currentUser.getId() + "/subscription";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        // زر إعادة التحقق من البريد
        btnVerifyEmail.setOnClickListener(v -> {
            vm.sendVerificationEmail().observe(this, sent -> {
                if (Boolean.TRUE.equals(sent)) {
                    Toast.makeText(this, R.string.verification_email_sent, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.err_send_verification, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }

}
