// EditProfileActivity.java
package com.proapp.obdcodes.ui.account;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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

import com.bumptech.glide.Glide;
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
    private static final String IMAGE_BASE_URL     = "https://obdcode.xyz/storage/";
    private static final int     REQUEST_PICK_IMAGE = 1001;
    private static final int     REQUEST_PERMISSION = 2001;

    private ImageView ivProfileImage;
    private Button    btnPickImage;
    private EditText  etUsername, etEmail, etPhone, etJobTitle;
    private Button    btnSave, btnVerifyEmail;

    private UserViewModel vm;
    private User          currentUser;
    private MultipartBody.Part avatarPart;  // لحفظ صورة الأفاتار قبل الرفع

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_edit_profile);

        // 1. ربط عناصر الواجهة
        ivProfileImage = findViewById(R.id.ivProfileImage);
        btnPickImage   = findViewById(R.id.btnPickImage);
        etUsername     = findViewById(R.id.etUsername);
        etEmail        = findViewById(R.id.etEmail);
        etPhone        = findViewById(R.id.etPhone);
        etJobTitle     = findViewById(R.id.etJobTitle);
        btnSave        = findViewById(R.id.btnSaveProfile);
        btnVerifyEmail = findViewById(R.id.btnVerifyEmail);

        // 2. تهيئة ViewModel وجلب بيانات المستخدم الحالية
        vm = new ViewModelProvider(this).get(UserViewModel.class);
        vm.getUserProfile().observe(this, this::populateUserData);

        // 3. إعداد زر اختيار الصورة مع إدارة الأذونات
        btnPickImage.setOnClickListener(v -> {
            String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                    ? Manifest.permission.READ_MEDIA_IMAGES
                    : Manifest.permission.READ_EXTERNAL_STORAGE;


            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    new AlertDialog.Builder(this)
                            .setTitle("صلاحية الوصول للصور")
                            .setMessage("نحتاج إذن الوصول لاختيار صورة الملف الشخصي.")
                            .setPositiveButton("موافق", (d, w) ->
                                    ActivityCompat.requestPermissions(
                                            this, new String[]{ permission }, REQUEST_PERMISSION))
                            .setNegativeButton("إلغاء", null)
                            .show();
                } else {
                    ActivityCompat.requestPermissions(
                            this, new String[]{ permission }, REQUEST_PERMISSION);
                }

            } else {
                openImagePicker();
            }
        });

        // 4. إعادة إرسال رابط التفعيل
        btnVerifyEmail.setOnClickListener(v ->
                vm.sendEmailVerification().observe(this, success -> {
                    String msg = success
                            ? "تم إرسال رابط التفعيل إلى بريدك الإلكتروني."
                            : "فشل إرسال رابط التفعيل، حاول مجدداً.";
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                })
        );

        // 5. حفظ التغييرات النصية — ثم رفع الصورة (إن وُجِدَت) — ثم إنهاء الشاشة
        btnSave.setOnClickListener(v -> {
            // أ. تجهيز طلب التحديث النصي
            UpdateProfileRequest req = new UpdateProfileRequest(
                    etUsername.getText().toString().trim(),
                    etEmail   .getText().toString().trim(),
                    etPhone   .getText().toString().trim(),
                    etJobTitle.getText().toString().trim()
            );

            // ب. إرسال الطلب
            vm.updateProfileData(req).observe(this, updated -> {
                if (updated != null) {
                    Toast.makeText(this, "تم حفظ التغييرات النصّية", Toast.LENGTH_SHORT).show();

                    // ج. إذا اختار المستخدم صورة، ارفعها
                    if (avatarPart != null) {
                        vm.updateProfileAvatar(avatarPart).observe(this, u2 -> {
                            Toast.makeText(
                                    this,
                                    u2 != null ? "تم تحديث الصورة بنجاح" : "فشل تحديث الصورة",
                                    Toast.LENGTH_SHORT
                            ).show();
                            finishWithResult();
                        });
                    } else {
                        // لا صورة، ننهي مباشرة
                        finishWithResult();
                    }

                } else {
                    Toast.makeText(this, "فشل حفظ التغييرات النصّية", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void populateUserData(User user) {
        if (user == null) {
            Toast.makeText(this, R.string.err_fetch_profile, Toast.LENGTH_SHORT).show();
            return;
        }
        currentUser = user;
        etUsername  .setText(user.getUsername());
        etEmail     .setText(user.getEmail());
        etPhone     .setText(user.getPhone());
        etJobTitle  .setText(user.getJobTitle());

        // تحميل الصورة السابقة
        String imgPath = user.getProfileImage();
        if (imgPath != null && !imgPath.isEmpty()) {
            String fullUrl = imgPath.startsWith("http")
                    ? imgPath
                    : IMAGE_BASE_URL + imgPath;
            Glide.with(this)
                    .load(fullUrl)
                    .circleCrop()
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile_sample)
                    .into(ivProfileImage);
        }

        btnVerifyEmail.setVisibility(
                user.getEmailVerifiedAt() == null ? View.VISIBLE : View.GONE
        );
    }

    private void openImagePicker() {
        Intent pick = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        startActivityForResult(pick, REQUEST_PICK_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openImagePicker();
        } else {
            Toast.makeText(this, "مطلوب إذن الوصول إلى الصور", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            Uri uri = data.getData();
            ivProfileImage.setImageURI(uri);

            String path = FileUtils.getPath(this, uri);
            if (path == null) {
                Toast.makeText(this, "فشل تحديد مسار الصورة", Toast.LENGTH_SHORT).show();
                return;
            }

            File file = new File(path);
            RequestBody rb = RequestBody.create(MediaType.parse("image/*"), file);
            avatarPart = MultipartBody.Part.createFormData(
                    "profile_image", file.getName(), rb
            );
        }
    }

    private void finishWithResult() {
        // يمكن إرسال نتيجة للشاشة الرئيسية إن احتجت onActivityResult
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }
}
