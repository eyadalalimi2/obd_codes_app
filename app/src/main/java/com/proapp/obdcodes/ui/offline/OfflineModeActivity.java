package com.proapp.obdcodes.ui.offline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.sync.SyncManager;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.utils.NetworkMonitor;
import com.proapp.obdcodes.util.SubscriptionUtils;

public class OfflineModeActivity extends BaseActivity {

    private Switch switchOffline;
    private Button btnDownload, btnViewOfflineCodes;
    private TextView tvStatus;
    private NetworkMonitor networkMonitor;
    private boolean offlineEnabled = false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // حماية الميزة (OFFLINE_MODE) - لا تهيئ الصفحة إلا بعد التأكد من الصلاحية
        SubscriptionUtils.checkFeatureAccess(this, "OFFLINE_MODE", () -> {
            runOnUiThread(() -> {
                setActivityLayout(R.layout.activity_offline_mode);
                setTitle("وضع الأوفلاين");

                switchOffline = findViewById(R.id.switchOffline);
                btnDownload = findViewById(R.id.btnDownloadData);
                btnViewOfflineCodes = findViewById(R.id.btnViewOfflineCodes);
                tvStatus = findViewById(R.id.tvStatus);

                // تحميل الحالة المخزنة مسبقًا
                offlineEnabled = getSharedPreferences("app_prefs", MODE_PRIVATE)
                        .getBoolean("offline_enabled", false);
                switchOffline.setChecked(offlineEnabled);
                tvStatus.setText(offlineEnabled ? "الوضع: مفعل" : "الوضع: معطل");

                switchOffline.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    offlineEnabled = isChecked;
                    tvStatus.setText(isChecked ? "الوضع: مفعل" : "الوضع: معطل");
                    getSharedPreferences("app_prefs", MODE_PRIVATE).edit()
                            .putBoolean("offline_enabled", isChecked).apply();
                });

                btnDownload.setOnClickListener(v -> {
                    if (!offlineEnabled) {
                        Toast.makeText(this, "قم بتفعيل وضع الأوفلاين أولاً", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("جاري تحميل الأكواد...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    SyncManager.downloadCodes(this, new SyncManager.SyncCallback() {
                        @Override
                        public void onSuccess(int count) {
                            runOnUiThread(() -> {
                                progressDialog.dismiss();
                                tvStatus.setText("تم تحميل " + count + " كود محليًا");
                                Toast.makeText(OfflineModeActivity.this,
                                        "تم تحميل الأكواد بنجاح", Toast.LENGTH_SHORT).show();
                            });
                        }

                        @Override
                        public void onFailure(String error) {
                            runOnUiThread(() -> {
                                progressDialog.dismiss();
                                tvStatus.setText("فشل في التحميل");
                                Toast.makeText(OfflineModeActivity.this,
                                        error, Toast.LENGTH_LONG).show();
                            });
                        }
                    });
                });

                btnViewOfflineCodes.setOnClickListener(v -> {
                    if (offlineEnabled) {
                        startActivity(new Intent(this, OfflineCodeListActivity.class));
                    } else {
                        Toast.makeText(this, "فعّل وضع الأوفلاين لعرض البيانات", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        networkMonitor = new NetworkMonitor(() -> {
            if (offlineEnabled) {
                SyncManager.downloadCodes(this, new SyncManager.SyncCallback() {
                    @Override
                    public void onSuccess(int count) {
                        runOnUiThread(() -> tvStatus.setText("تم تحديث " + count + " كود تلقائيًا"));
                    }

                    @Override
                    public void onFailure(String error) {
                        runOnUiThread(() -> tvStatus.setText("فشل التحديث التلقائي: " + error));
                    }
                });
            }
        });
        networkMonitor.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (networkMonitor != null) networkMonitor.unregister(this);
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }
}
