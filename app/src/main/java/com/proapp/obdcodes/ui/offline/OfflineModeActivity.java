package com.proapp.obdcodes.ui.offline;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.proapp.obdcodes.R;

public class OfflineModeActivity extends AppCompatActivity {

    private Switch switchOffline;
    private Button btnDownload;
    private TextView tvStatus;

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
                Toast.makeText(this, "جاري تحميل الأكواد... (وهمي)", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "قم بتفعيل وضع الأوفلاين أولاً", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
