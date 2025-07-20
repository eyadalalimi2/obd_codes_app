package com.proapp.obdcodes.ui.offline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.sync.SyncManager;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.util.SubscriptionUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OfflineModeActivity extends BaseActivity {

    private Switch switchOffline;
    private ImageView ivOfflineStatus;
    private TextView tvStatus, tvLastSync;
    private Button btnDownload, btnViewOfflineCodes;
    private SharedPreferences prefs;
    private boolean offlineEnabled;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SubscriptionUtils.checkFeatureAccess(
                this, "OFFLINE_MODE", this::initOfflineUI
        );
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
                    Toast.makeText(
                            this, R.string.enable_offline_first,
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                // عرض ProgressDialog
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage(getString(R.string.downloading_codes));
                progressDialog.setCancelable(false);
                progressDialog.show();

                SyncManager.downloadCodes(this, new SyncManager.SyncCallback() {
                    @Override
                    public void onSuccess(int count) {
                        runOnUiThread(() -> {
                            progressDialog.dismiss();
                            String date = new SimpleDateFormat(
                                    "yyyy-MM-dd", Locale.getDefault()
                            ).format(new Date());
                            prefs.edit().putString("last_sync_date", date).apply();
                            tvLastSync.setText(
                                    getString(R.string.last_synced_on, date)
                            );
                            Toast.makeText(
                                    OfflineModeActivity.this,
                                    getString(R.string.codes_downloaded, count),
                                    Toast.LENGTH_SHORT
                            ).show();
                        });
                    }

                    @Override
                    public void onFailure(String error) {
                        runOnUiThread(() -> {
                            progressDialog.dismiss();
                            Toast.makeText(
                                    OfflineModeActivity.this,
                                    error, Toast.LENGTH_LONG
                            ).show();
                        });
                    }
                });
            });

            btnViewOfflineCodes.setOnClickListener(v -> {
                if (offlineEnabled) {
                    startActivity(new Intent(
                            this, OfflineCodeListActivity.class
                    ));
                } else {
                    Toast.makeText(
                            this, R.string.enable_offline_first,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        });
    }

    private void updateStatusUI() {
        int tint = offlineEnabled
                ? R.color.accent
                : R.color.text_secondary;
        ivOfflineStatus.setImageTintList(
                ContextCompat.getColorStateList(this, tint)
        );
        tvStatus.setText(
                getString(
                        offlineEnabled
                                ? R.string.status_enabled
                                : R.string.status_disabled
                )
        );
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return true;
    }
}
