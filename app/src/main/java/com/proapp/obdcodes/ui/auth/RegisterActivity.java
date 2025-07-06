package com.proapp.obdcodes.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.databinding.ActivityRegisterBinding;
import com.proapp.obdcodes.network.ApiClient;
import com.proapp.obdcodes.ui.home.HomeActivity;
import com.proapp.obdcodes.viewmodel.AuthViewModel;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(AuthViewModel.class);

        binding.tvSignupTab.setOnClickListener(v -> registerWithEmail());
        binding.tvVersion.setOnClickListener(v -> finish());
    }

    private void registerWithEmail() {
        String name  = binding.etName.getText().toString().trim();
        String email = binding.etSignupEmail.getText().toString().trim();
        String pass  = binding.etSignupPassword.getText().toString().trim();

        if (name.isEmpty() ||
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() ||
                pass.length()<6 ||
                !binding.cbAgree.isChecked()
        ) {
            if (name.isEmpty()) binding.etName.setError(getString(R.string.err_fill_all));
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                binding.etSignupEmail.setError(getString(R.string.err_invalid_email));
            else if (pass.length()<6)
                binding.etSignupPassword.setError(getString(R.string.err_invalid_pass));
            else
                Toast.makeText(this, R.string.err_agree, Toast.LENGTH_SHORT).show();
            return;
        }

        setLoading(true);
        viewModel.register(name, email, pass).observe(this, resp -> {
            setLoading(false);
            if (resp!=null && resp.isSuccess() && resp.getToken()!=null) {
                saveLogin(resp.getToken());
                navigateHome();
            } else {
                showToast(R.string.reg_failed);
            }
        });
    }

    private void setLoading(boolean loading) {
        binding.progressAuth.setVisibility(loading? View.VISIBLE: View.GONE);
        binding.tvSignupTab.setEnabled(!loading);
        binding.tvVersion.setEnabled(!loading);
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

    private void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }
}
