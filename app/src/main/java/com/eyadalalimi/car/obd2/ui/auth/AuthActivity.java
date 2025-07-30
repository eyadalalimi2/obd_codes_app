// AuthActivity.java
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

public class AuthActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1001;
    private AuthViewModel viewModel;
    private GoogleSignInClient googleClient;

    // login views
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvForgot, tvCreateAccount;
    private ImageView ivGoogleLogin, ivFacebookLogin, ivAppleLogin;
    private ProgressBar progressLogin;

    // register views
    private EditText etRegUsername, etRegEmail, etRegPassword, etRegConfirm;
    private Button btnRegister;
    private TextView tvHaveAccount;
    private ImageView ivGoogleReg, ivFacebookReg, ivAppleReg;
    private ProgressBar progressReg;

    private enum Mode { LOGIN, REGISTER }
    private Mode currentMode = Mode.LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(AuthViewModel.class);

        // إعداد GoogleSignIn
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.server_client_id))
                .build();
        googleClient = GoogleSignIn.getClient(this, gso);

        showLogin();  // شاشة البداية
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

    private void showLogin() {
        currentMode = Mode.LOGIN;
        setContentView(R.layout.activity_login);
        hideSystemUI();
        bindLoginViews();
        bindLoginActions();
        observeLogin();
    }

    private void showRegister() {
        currentMode = Mode.REGISTER;
        setContentView(R.layout.activity_register);
        hideSystemUI();
        bindRegisterViews();
        bindRegisterActions();
        observeRegister();
    }

    private void bindLoginViews() {
        etEmail          = findViewById(R.id.emailField);
        etPassword       = findViewById(R.id.passwordField);
        btnLogin         = findViewById(R.id.btnLogin);
        tvForgot         = findViewById(R.id.tvForgot);
        tvCreateAccount  = findViewById(R.id.tvCreateAccount);
        ivGoogleLogin    = findViewById(R.id.ivGoogleLogin);
        ivFacebookLogin  = findViewById(R.id.ivFacebookLogin);
        ivAppleLogin     = findViewById(R.id.ivAppleLogin);
        progressLogin    = findViewById(R.id.progressAuth);
    }

    private void bindLoginActions() {
        btnLogin.setOnClickListener(v -> loginWithEmail());
        tvCreateAccount.setOnClickListener(v -> showRegister());
        tvForgot.setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class))
        );
        ivGoogleLogin.setOnClickListener(v ->
                startActivityForResult(googleClient.getSignInIntent(), RC_SIGN_IN)
        );
        // فيسبوك/آبل حسب الحاجة...
    }

    private void bindRegisterViews() {
        etRegUsername    = findViewById(R.id.etRegUsername);
        etRegEmail       = findViewById(R.id.etRegEmail);
        etRegPassword    = findViewById(R.id.etRegPassword);
        etRegConfirm     = findViewById(R.id.etRegConfirm);
        btnRegister      = findViewById(R.id.btnRegister);
        tvHaveAccount    = findViewById(R.id.tvHaveAccount);
        ivGoogleReg      = findViewById(R.id.ivGoogleReg);
        ivFacebookReg    = findViewById(R.id.ivFacebookReg);
        ivAppleReg       = findViewById(R.id.ivAppleReg);
        progressReg      = findViewById(R.id.progressAuthReg);
    }

    private void bindRegisterActions() {
        btnRegister.setOnClickListener(v -> registerWithEmail());
        tvHaveAccount.setOnClickListener(v -> showLogin());
        ivGoogleReg.setOnClickListener(v ->
                startActivityForResult(googleClient.getSignInIntent(), RC_SIGN_IN)
        );
        // فيسبوك/آبل حسب الحاجة...
    }

    private void observeLogin() {
        viewModel.getLoginResult().observe(this, result -> {
            setLoading(false);
            if (result == null) {
                // استدعاء الصيغة الخاصة بالموارد (int)
                showToast(R.string.login_failed);
                return;
            }
            if (result.isOk() && result.getToken() != null) {
                saveLogin(result.getToken());
                navigateHome();
            } else {
                // استدعاء الصيغة الخاصة بالنصوص (String)
                showToast(result.getMessage());
            }
        });

    }

    private void observeRegister() {
        viewModel.getRegisterResult().observe(this, result -> {
            setLoading(false);
            if (result == null) {
                showToast(R.string.reg_failed);
                return;
            }
            if (result.isOk() && result.getToken() != null) {
                saveLogin(result.getToken());
                startActivity(new Intent(this, EmailVerificationActivity.class));
                finish();
            } else {
                showToast(result.getMessage());
            }
        });

    }
    private boolean isProbablyUsingVpn() {
        // طريقة مبسطة: يمكنك فحص الشبكات المثبتة أو IP العام إن توفر لك
        // هنا سنجعل المستخدم يقرر بنفسه
        return false; // أو true بناءً على فحصك
    }


    private void loginWithEmail() {
        if (isProbablyUsingVpn()) {
            showToast("يُرجى تعطيل VPN ثم المحاولة مرة أخرى.");
            return;
        }
        String email = etEmail.getText().toString().trim();
        String pass  = etPassword.getText().toString().trim();
        if (!validate(email, pass)) return;
        setLoading(true);
        viewModel.login(email, pass);
    }

    private void registerWithEmail() {
        if (isProbablyUsingVpn()) {
            showToast("يُرجى تعطيل VPN ثم المحاولة مرة أخرى.");
            return;
        }
        String username = etRegUsername.getText().toString().trim();
        String email = etRegEmail.getText().toString().trim();
        String pass  = etRegPassword.getText().toString().trim();
        String conf  = etRegConfirm.getText().toString().trim();
        if (!validate(email, pass, conf)) return;
        setLoading(true);
        viewModel.register(username, email, pass);
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

    private boolean validate(String email, String pass, String conf) {
        if (email.isEmpty() || pass.isEmpty() || conf.isEmpty()) {
            showToast(R.string.err_fill_all);
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etRegEmail.setError(getString(R.string.err_invalid_email));
            return false;
        }
        if (pass.length() < 6) {
            etRegPassword.setError(getString(R.string.err_invalid_pass));
            return false;
        }
        if (!pass.equals(conf)) {
            etRegConfirm.setError(getString(R.string.err_pass_mismatch));
            return false;
        }
        return true;
    }

    private void setLoading(boolean loading) {
        if (currentMode == Mode.LOGIN) {
            progressLogin.setVisibility(loading ? View.VISIBLE : View.GONE);
            btnLogin.setEnabled(!loading);
            ivGoogleLogin.setEnabled(!loading);
            ivFacebookLogin.setEnabled(!loading);
            ivAppleLogin.setEnabled(!loading);
        } else {
            progressReg.setVisibility(loading ? View.VISIBLE : View.GONE);
            btnRegister.setEnabled(!loading);
            ivGoogleReg.setEnabled(!loading);
            ivFacebookReg.setEnabled(!loading);
            ivAppleReg.setEnabled(!loading);
        }
    }

    private void saveLogin(String token) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        prefs.edit()
                .putString("auth_token", token)
                .putBoolean("is_logged_in", true)
                .apply();
        ApiClient.reset();
    }

    private void navigateHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void showToast(int res) {
        Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
    }
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int req, int res, @Nullable Intent data) {
        super.onActivityResult(req, res, data);
        if (req == RC_SIGN_IN) {
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
