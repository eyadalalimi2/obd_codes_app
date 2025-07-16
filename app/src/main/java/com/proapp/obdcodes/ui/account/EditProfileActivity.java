package com.proapp.obdcodes.ui.account;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.model.UpdateProfileRequest;
import com.proapp.obdcodes.network.model.User;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.util.FileUtils;
import com.proapp.obdcodes.viewmodel.UserViewModel;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileActivity extends BaseActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 1001;
    private static final int REQUEST_CODE_PERMISSION = 2001;

    private ImageView ivProfileImage;
    private Button btnPickImage;
    private EditText etUsername, etEmail, etPhone, etJobTitle;
    private Button btnSave, btnVerifyEmail;
    private UserViewModel vm;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_edit_profile);

        // ربط عناصر الواجهة
        ivProfileImage = findViewById(R.id.ivProfileImage);
        btnPickImage   = findViewById(R.id.btnPickImage);
        etUsername     = findViewById(R.id.etUsername);
        etEmail        = findViewById(R.id.etEmail);
        etPhone        = findViewById(R.id.etPhone);
        etJobTitle     = findViewById(R.id.etJobTitle);
        btnSave        = findViewById(R.id.btnSaveProfile);
        btnVerifyEmail = findViewById(R.id.btnVerifyEmail);

        // تهيئة ViewModel
        vm = new ViewModelProvider(this).get(UserViewModel.class);

        // جلب البيانات الحالية للمستخدم
        vm.getUserProfile().observe(this, user -> {
            if (user != null) {
                currentUser = user;
                etUsername.setText(user.getUsername());
                etEmail   .setText(user.getEmail());
                etPhone   .setText(user.getPhone());
                etJobTitle.setText(user.getJobTitle());

                // إظهار أو إخفاء زر التحقق
                btnVerifyEmail.setVisibility(
                        user.getEmailVerifiedAt() == null ? View.VISIBLE : View.GONE
                );

                // (اختياري) تحميل صورة الملف الشخصي:
                // Glide.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);

            } else {
                Toast.makeText(this, R.string.err_fetch_profile, Toast.LENGTH_SHORT).show();
            }
        });

        // اختيار صورة جديدة مع التحقق من الأذونات
        btnPickImage.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE },
                        REQUEST_CODE_PERMISSION);

            } else {
                openImagePicker();
            }
        });

        // إعادة إرسال رابط التفعيل
        btnVerifyEmail.setOnClickListener(v -> {
            vm.sendVerificationEmail().observe(this, sent -> {
                String msg = Boolean.TRUE.equals(sent)
                        ? getString(R.string.verification_email_sent)
                        : getString(R.string.resend_verification);
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            });
        });

        // حفظ التغييرات النصية
        btnSave.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String email    = etEmail   .getText().toString().trim();
            String phone    = etPhone   .getText().toString().trim();
            String jobTitle = etJobTitle.getText().toString().trim();

            vm.updateProfileData(new UpdateProfileRequest(username, email, phone, jobTitle))
                    .observe(this, updated -> {
                        String msg = (updated != null)
                                ? "تم حفظ التغييرات بنجاح"
                                : "فشل حفظ التغييرات";
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void openImagePicker() {
        Intent pick = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pick, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openImagePicker();
        } else {
            Toast.makeText(this, "مطلوب إذن الوصول إلى الصور", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE
                && resultCode == RESULT_OK
                && data != null) {

            Uri uri = data.getData();
            ivProfileImage.setImageURI(uri);

            // تحويل Uri إلى ملف (FileUtils.getPath)
            String path = FileUtils.getPath(this, uri);
            if (path == null) {
                Toast.makeText(this, "فشل تحديد مسار الصورة", Toast.LENGTH_SHORT).show();
                return;
            }

            File file = new File(path);
            RequestBody bodyRequest = RequestBody.create(
                    MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData(
                    "profile_image", file.getName(), bodyRequest);

            vm.updateProfileAvatar(part).observe(this, updated -> {
                String msg = (updated != null)
                        ? "تم تحديث الصورة بنجاح"
                        : "فشل تحديث الصورة";
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }
}
