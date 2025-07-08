package com.proapp.obdcodes.ui.offline;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.sync.SyncManager;
import android.content.Intent;
import com.proapp.obdcodes.utils.NetworkMonitor;
import com.proapp.obdcodes.sync.SyncManager;


public class OfflineModeActivity extends AppCompatActivity {

    private Switch switchOffline;
    private Button btnDownload;
    private TextView tvStatus;
    private NetworkMonitor networkMonitor;
    private boolean offlineEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_mode);
        setTitle("وضع الأوفلاين");

        switchOffline = findViewById(R.id.switchOffline);
        btnDownload = findViewById(R.id.btnDownloadData);
        tvStatus = findViewById(R.id.tvStatus);

        switchOffline.setOnCheckedChangeListener((buttonView, isChecked) -> {
            offlineEnabled = isChecked;
            tvStatus.setText(offlineEnabled ? "الوضع: مفعل" : "الوضع: معطل");
        });

        btnDownload.setOnClickListener(v -> {
            if (offlineEnabled) {
                tvStatus.setText("جاري تحميل الأكواد...");
                SyncManager.downloadCodes(this, new SyncManager.SyncCallback() {
                    @Override
                    public void onSuccess(int count) {
                        runOnUiThread(() -> {
                            tvStatus.setText("تم تحميل " + count + " كود محليًا");
                            Toast.makeText(OfflineModeActivity.this,
                                    "تم تحميل الأكواد بنجاح", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onFailure(String error) {
                        runOnUiThread(() -> {
                            tvStatus.setText("فشل في التحميل");
                            Toast.makeText(OfflineModeActivity.this,
                                    error, Toast.LENGTH_LONG).show();
                        });
                    }
                });
            } else {
                Toast.makeText(this, "قم بتفعيل وضع الأوفلاين أولاً", Toast.LENGTH_SHORT).show();
            }

        });
        Button btnViewOfflineCodes = findViewById(R.id.btnViewOfflineCodes);
        btnViewOfflineCodes.setOnClickListener(v -> {
            if (offlineEnabled) {
                startActivity(new Intent(this, OfflineCodeListActivity.class));
            } else {
                Toast.makeText(this, "فعّل وضع الأوفلاين لعرض البيانات", Toast.LENGTH_SHORT).show();
            }
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
        if (networkMonitor != null) {
            networkMonitor.unregister(this);
        }
    }
}
