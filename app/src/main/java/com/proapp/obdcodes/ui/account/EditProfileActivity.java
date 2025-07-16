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
import com.proapp.obdcodes.network.model.User;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.viewmodel.UserViewModel;

public class EditProfileActivity extends BaseActivity {

    private EditText etUsername, etEmail, etPhone, etJobTitle;
   
    private Button btnSave,  btnVerifyEmail;
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




        // زر إعادة التحقق من البريد
        btnVerifyEmail.setOnClickListener(v -> {
            vm.sendVerificationEmail().observe(this, sent -> {
                if (Boolean.TRUE.equals(sent)) {
                    Toast.makeText(this, R.string.verification_email_sent, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.resend_verification, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }

}
