package com.eyadalalimi.car.obd2.ui.offline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.sync.SyncManager;
import com.eyadalalimi.car.obd2.ui.base.BaseActivity;
import com.eyadalalimi.car.obd2.util.SubscriptionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * واجهة وضع عدم الاتصال بعد تحديثها للتخلي عن ProgressDialog واستخدام ProgressBar للمزامنة.
 */
public class OfflineModeActivity extends BaseActivity {

    private Switch switchOffline;
    private ImageView ivOfflineStatus;
    private TextView tvStatus, tvLastSync;
    private Button btnDownload, btnViewOfflineCodes;
    private SharedPreferences prefs;
    private boolean offlineEnabled;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SubscriptionUtils.checkFeatureAccess(this, "OFFLINE_MODE", this::initOfflineUI);
    }

    private void initOfflineUI() {
        runOnUiThread(() -> {
            setActivityLayout(R.layout.activity_offline_mode);
            setTitle(getString(R.string.offline_mode));

            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            switchOffline       = findViewById(R.id.switchOffline);
            ivOfflineStatus     = findViewById(R.id.ivOfflineStatus);
            tvStatus            = findViewById(R.id.tvStatus);
            tvLastSync          = findViewById(R.id.tvLastSync);
            btnDownload         = findViewById(R.id.btnDownloadData);
            btnViewOfflineCodes = findViewById(R.id.btnViewOfflineCodes);
            progressBar         = findViewById(R.id.progressBarOffline); // تأكد من وجود ProgressBar في التخطيط

            // استرجاع الحالة والتاريخ
            offlineEnabled = prefs.getBoolean("offline_enabled", false);
            switchOffline.setChecked(offlineEnabled);
            updateStatusUI();

            String lastSync = prefs.getString("last_sync_date", "-");
            tvLastSync.setText(getString(R.string.last_synced_on, lastSync));

            switchOffline.setOnCheckedChangeListener((button, isChecked) -> {
                offlineEnabled = isChecked;
                prefs.edit().putBoolean("offline_enabled", isChecked).apply();
                updateStatusUI();
            });

            btnDownload.setOnClickListener(v -> {
                if (!offlineEnabled) {
                    Toast.makeText(this, R.string.enable_offline_first, Toast.LENGTH_SHORT).show();
                    return;
                }
                // إظهار ProgressBar والبدء في تحميل الأكواد
                progressBar.setVisibility(ProgressBar.VISIBLE);
                SyncManager.downloadCodes(this, new SyncManager.SyncCallback() {
                    @Override
                    public void onSuccess(int count) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(ProgressBar.GONE);
                            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                            prefs.edit().putString("last_sync_date", date).apply();
                            tvLastSync.setText(getString(R.string.last_synced_on, date));
                            Toast.makeText(OfflineModeActivity.this,
                                    getString(R.string.codes_downloaded, count),
                                    Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onFailure(String error) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(ProgressBar.GONE);
                            Toast.makeText(OfflineModeActivity.this, error, Toast.LENGTH_LONG).show();
                        });
                    }
                });
            });

            btnViewOfflineCodes.setOnClickListener(v -> {
                if (offlineEnabled) {
                    startActivity(new Intent(this, OfflineCodeListActivity.class));
                } else {
                    Toast.makeText(this, R.string.enable_offline_first, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void updateStatusUI() {
        int tint = offlineEnabled ? R.color.accent : R.color.text_secondary;
        ivOfflineStatus.setImageTintList(ContextCompat.getColorStateList(this, tint));
        tvStatus.setText(getString(offlineEnabled ? R.string.status_enabled : R.string.status_disabled));
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return true;
    }
}
