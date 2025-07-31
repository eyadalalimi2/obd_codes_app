package com.eyadalalimi.car.obd2.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eyadalalimi.car.obd2.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

/**
 * نشاط للتعامل مع مصادقة الهاتف باستخدام Firebase.
 * يسمح بإدخال رقم الهاتف، إرسال رمز التحقق، ثم إدخال الرمز وتسجيل الدخول.
 */
public class PhoneAuthActivity extends AppCompatActivity {

    private EditText etPhoneNumber, etVerificationCode;
    private Button btnSendCode, btnVerifyCode;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        // تهيئة FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        etPhoneNumber     = findViewById(R.id.et_phone_number);
        etVerificationCode= findViewById(R.id.et_verification_code);
        btnSendCode       = findViewById(R.id.btn_send_code);
        btnVerifyCode     = findViewById(R.id.btn_verify_code);
        progressBar       = findViewById(R.id.progressBar);

        btnSendCode.setOnClickListener(v -> sendVerificationCode());
        btnVerifyCode.setOnClickListener(v -> verifyCode());
    }

    /** إرسال رمز التحقق إلى رقم الهاتف المدخل */
    private void sendVerificationCode() {
        String phone = etPhoneNumber.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            etPhoneNumber.setError("أدخل رقم الهاتف");
            return;
        }
        // إظهار مؤشر التحميل وتعطيل الأزرار
        progressBar.setVisibility(View.VISIBLE);
        btnSendCode.setEnabled(false);

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)               // رقم الهاتف المطلوب
                        .setTimeout(60L, TimeUnit.SECONDS)   // مهلة التحقق
                        .setActivity(this)                   // النشاط الحالي (لـ callback)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                // التحقق التلقائي (في بعض الأجهزة) أو إدخال الرمز يدوياً
                                signInWithPhoneAuthCredential(credential);
                            }
                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressBar.setVisibility(View.GONE);
                                btnSendCode.setEnabled(true);
                                Toast.makeText(PhoneAuthActivity.this,
                                        "فشل في إرسال الرمز: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                            @Override
                            public void onCodeSent(@NonNull String verId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                // حفظ verId و token لإعادة الإرسال أو التحقق
                                verificationId = verId;
                                resendToken = token;
                                progressBar.setVisibility(View.GONE);
                                btnSendCode.setEnabled(true);
                                Toast.makeText(PhoneAuthActivity.this,
                                        "تم إرسال الرمز، يرجى إدخاله",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    /** التحقق من الرمز الذي أدخله المستخدم وتسجيل الدخول */
    private void verifyCode() {
        String code = etVerificationCode.getText().toString().trim();
        if (TextUtils.isEmpty(code) || verificationId == null) {
            Toast.makeText(this, "أدخل رمز التحقق الصحيح", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    /** إتمام عملية تسجيل الدخول بالاعتماد على credential */
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        // نجاح التحقق، يمكنك الانتقال لواجهة التطبيق الرئيسية
                        Toast.makeText(this, "تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT).show();
                        // TODO: navigate to HomeActivity or handle signed-in user
                        finish();
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(this, "رمز غير صحيح", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "فشل في تسجيل الدخول: "
                                    + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
