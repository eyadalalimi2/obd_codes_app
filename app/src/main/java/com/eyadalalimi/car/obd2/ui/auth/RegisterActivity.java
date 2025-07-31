// RegisterActivity.java
package com.eyadalalimi.car.obd2.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.network.ApiClient;
import com.eyadalalimi.car.obd2.ui.home.HomeActivity;
import com.eyadalalimi.car.obd2.viewmodel.AuthViewModel;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * شاشة تسجيل مستخدم جديد.
 * تم تحديث طريقة saveLogin لتخزين التوكن في تفضيلات مشفرة وتفضيلات عادية ولتصحيح ربط حقل التأكيد.
 */
public class RegisterActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1001;

    private EditText etEmail, etPassword, etConfirm;
    private Button btnSignUp;
    private TextView tvAlreadyAccount;
    private ImageView ivGoogleSignIn, ivFacebookSignIn, ivAppleSignIn;
    private ProgressBar progressAuth;
    private AuthViewModel viewModel;
    private GoogleSignInClient googleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        hideSystemUI();
        initViews();
        initViewModel();
        initGoogleSignIn();
        bindActions();
    }

    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
            getWindow().getInsetsController().hide(
                    android.view.WindowInsets.Type.statusBars()
                            | android.view.WindowInsets.Type.navigationBars()
            );
            getWindow().getInsetsController().setSystemBarsBehavior(
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            );
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
        }
    }

    private void initViews() {
        etEmail          = findViewById(R.id.emailField);
        etPassword       = findViewById(R.id.passwordField);
        etConfirm        = findViewById(R.id.etRegConfirm); // ربط صحيح لحقل تأكيد كلمة المرور
        btnSignUp        = findViewById(R.id.btnRegister);
        tvAlreadyAccount = findViewById(R.id.tvHaveAccount);
        ivGoogleSignIn   = findViewById(R.id.ivGoogleReg);
        ivFacebookSignIn = findViewById(R.id.ivFacebookReg);
        ivAppleSignIn    = findViewById(R.id.ivAppleReg);
        progressAuth     = findViewById(R.id.progressAuth);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(AuthViewModel.class);

        viewModel.getRegisterResult().observe(this, result -> {
            setLoading(false);
            if (result == null) {
                showToast(R.string.reg_failed);
                return;
            }
            if (result.isOk() && result.getToken() != null) {
                saveLogin(result.getToken());
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            } else {
                showToast(result.getMessage());
            }
        });
    }

    private void initGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.server_client_id))
                .build();
        googleClient = GoogleSignIn.getClient(this, gso);
    }

    private void bindActions() {
        btnSignUp.setOnClickListener(v -> attemptRegister());

        tvAlreadyAccount.setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class))
        );

        ivGoogleSignIn.setOnClickListener(v ->
                startActivityForResult(googleClient.getSignInIntent(), RC_SIGN_IN)
        );
        ivFacebookSignIn.setOnClickListener(v ->
                Toast.makeText(this, "Facebook sign-in غير متوفر", Toast.LENGTH_SHORT).show()
        );
        ivAppleSignIn.setOnClickListener(v ->
                Toast.makeText(this, "Apple sign-in غير متوفر", Toast.LENGTH_SHORT).show()
        );
    }

    private void attemptRegister() {
        String email   = etEmail.getText().toString().trim();
        String pass    = etPassword.getText().toString().trim();
        String confirm = etConfirm.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            showToast(R.string.err_fill_all);
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.err_invalid_email));
            return;
        }
        if (pass.length() < 6) {
            etPassword.setError(getString(R.string.err_invalid_pass));
            return;
        }
        if (!pass.equals(confirm)) {
            etConfirm.setError(getString(R.string.err_pass_mismatch));
            return;
        }

        setLoading(true);
        viewModel.register("", email, pass);
    }

    private void setLoading(boolean loading) {
        progressAuth.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnSignUp       .setEnabled(!loading);
        ivGoogleSignIn  .setEnabled(!loading);
        ivFacebookSignIn.setEnabled(!loading);
        ivAppleSignIn   .setEnabled(!loading);
    }

    /**
     * حفظ التوكن في تفضيلات مشفرة وتفضيلات عادية لضمان قراءته من جميع أجزاء التطبيق.
     */
    private void saveLogin(String token) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences securePrefs = EncryptedSharedPreferences.create(
                    "secure_prefs",
                    masterKeyAlias,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            securePrefs.edit()
                    .putString("auth_token", token)
                    .putBoolean("is_logged_in", true)
                    .apply();

            // حفظ نسخة في التفضيلات الافتراضية للتوافق
            SharedPreferences fallbackPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            fallbackPrefs.edit()
                    .putString("auth_token", token)
                    .putBoolean("is_logged_in", true)
                    .apply();
        } catch (GeneralSecurityException | IOException e) {
            // في حالة فشل التخزين المشفر، استخدم التفضيلات الافتراضية
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit()
                    .putString("auth_token", token)
                    .putBoolean("is_logged_in", true)
                    .apply();
        }
        ApiClient.reset();
    }

    private void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount acct = task.getResult(ApiException.class);
                if (acct != null) {
                    setLoading(true);
                    viewModel.loginWithGoogle(acct.getIdToken());
                }
            } catch (ApiException e) {
                setLoading(false);
                showToast(R.string.google_signin_failed);
            }
        }
    }
}
