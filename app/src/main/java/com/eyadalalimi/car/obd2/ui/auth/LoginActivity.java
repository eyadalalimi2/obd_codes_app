// LoginActivity.java
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.eyadalalimi.car.obd2.ui.auth.ForgotPasswordActivity;
import com.eyadalalimi.car.obd2.ui.auth.RegisterActivity;
import com.eyadalalimi.car.obd2.viewmodel.AuthViewModel;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * شاشة تسجيل الدخول بحساب البريد الإلكتروني أو Google.
 * تم تحديث هذه النسخة لاستخدام EncryptedSharedPreferences وActivityResultLauncher.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnSignIn;
    private TextView tvForgot, tvCreateAccount;
    private ImageView ivGoogleSignIn, ivFacebookSignIn, ivAppleSignIn;
    private ProgressBar progressAuth;
    private AuthViewModel viewModel;
    private GoogleSignInClient googleClient;

    private ActivityResultLauncher<Intent> googleSignInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        hideSystemUI();
        initViews();
        initViewModel();
        initGoogleSignIn();
        initActivityResultLauncher();
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
        etEmail         = findViewById(R.id.emailField);
        etPassword      = findViewById(R.id.passwordField);
        btnSignIn       = findViewById(R.id.btnLogin);
        tvForgot        = findViewById(R.id.tvForgot);
        tvCreateAccount = findViewById(R.id.tvCreateAccount);
        ivGoogleSignIn  = findViewById(R.id.ivGoogleLogin);
        ivFacebookSignIn= findViewById(R.id.ivFacebookLogin);
        ivAppleSignIn   = findViewById(R.id.ivAppleLogin);
        progressAuth    = findViewById(R.id.progressAuth);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(AuthViewModel.class);

        viewModel.getLoginResult().observe(this, result -> {
            setLoading(false);
            if (result == null) {
                showToast(R.string.login_failed);
                return;
            }
            if (result.isOk() && result.getToken() != null) {
                saveLogin(result.getToken());
                navigateHome();
            } else {
                showToast(result.getMessage());
            }
        });

        viewModel.getGoogleLoginResult().observe(this, result -> {
            setLoading(false);
            if (result == null) {
                showToast(R.string.login_failed);
                return;
            }
            if (result.isOk() && result.getToken() != null) {
                saveLogin(result.getToken());
                navigateHome();
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

    /** إعداد ActivityResultLauncher لمعالجة نتيجة تسجيل الدخول عبر Google. */
    private void initActivityResultLauncher() {
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            if (account != null) {
                                setLoading(true);
                                viewModel.loginWithGoogle(account.getIdToken());
                            }
                        } catch (ApiException e) {
                            setLoading(false);
                            showToast(R.string.google_signin_failed);
                        }
                    }
                });
    }

    private void bindActions() {
        btnSignIn.setOnClickListener(v -> loginWithEmail());

        tvForgot.setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class))
        );
        tvCreateAccount.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );

        ivGoogleSignIn.setOnClickListener(v ->
                googleSignInLauncher.launch(googleClient.getSignInIntent())
        );
        ivFacebookSignIn.setOnClickListener(v ->
                Toast.makeText(this, "Facebook sign-in غير متوفر", Toast.LENGTH_SHORT).show()
        );
        ivAppleSignIn.setOnClickListener(v ->
                Toast.makeText(this, "Apple sign-in غير متوفر", Toast.LENGTH_SHORT).show()
        );
    }

    private void loginWithEmail() {
        String email = etEmail.getText().toString().trim();
        String pass  = etPassword.getText().toString().trim();
        if (!validate(email, pass)) return;

        setLoading(true);
        viewModel.login(email, pass);
    }

    private boolean validate(String email, String pass) {
        if (email.isEmpty() || pass.isEmpty()) {
            showToast(R.string.err_fill_all);
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.err_invalid_email));
            return false;
        }
        if (pass.length() < 6) {
            etPassword.setError(getString(R.string.err_invalid_pass));
            return false;
        }
        return true;
    }

    private void setLoading(boolean loading) {
        progressAuth.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnSignIn.setEnabled(!loading);
        ivGoogleSignIn.setEnabled(!loading);
        ivFacebookSignIn.setEnabled(!loading);
        ivAppleSignIn.setEnabled(!loading);
    }

    /**
     /**
     * حفظ التوكن في تفضيلات مشفرة وتفضيلات عادية لضمان التوافق.
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

            // حفظ التوكن أيضاً في التفضيلات الافتراضية (للتوافق مع أجزاء أخرى من التطبيق)
            SharedPreferences fallbackPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            fallbackPrefs.edit()
                    .putString("auth_token", token)
                    .putBoolean("is_logged_in", true)
                    .apply();
        } catch (GeneralSecurityException | IOException e) {
            // إذا فشل التخزين المشفر، استخدم التفضيلات الافتراضية كحل احتياطي
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit()
                    .putString("auth_token", token)
                    .putBoolean("is_logged_in", true)
                    .apply();
        }
        // إعادة تهيئة عميل الشبكة
        ApiClient.reset();
    }

    private void navigateHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
