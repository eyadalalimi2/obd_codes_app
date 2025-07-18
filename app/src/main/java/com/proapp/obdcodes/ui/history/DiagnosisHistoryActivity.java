package com.proapp.obdcodes.ui.history;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.ui.diagnosis.SymptomDiagnosisActivity;
import com.proapp.obdcodes.util.SubscriptionUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class DiagnosisHistoryActivity extends BaseActivity {

    private ListView historyList;
    private View emptyView;
    private Button btnClearHistory;
    private Button btnStartNew;
    private List<String> history;
    private DiagnosisHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SubscriptionUtils.checkFeatureAccess(
                this,
                "DIAGNOSIS_HISTORY",
                this::initHistoryView
        );
    }

    private void initHistoryView() {
        runOnUiThread(() -> {
            setActivityLayout(R.layout.activity_diagnosis_history);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(getString(R.string.history_title));
            }

            historyList     = findViewById(R.id.lvHistory);
            emptyView       = findViewById(R.id.emptyView);
            btnClearHistory = findViewById(R.id.btnClearHistory);
            btnStartNew     = findViewById(R.id.btnStartNew);

            // تحميل السجل من SharedPreferences
            history = loadHistory();

            // إعداد Adapter
            adapter = new DiagnosisHistoryAdapter(this, history);
            historyList.setAdapter(adapter);
            historyList.setEmptyView(emptyView);

            // زر "تشخيص جديد" يعود للصفحة السابقة
            btnStartNew.setOnClickListener(v -> {
                Intent intent = new Intent(DiagnosisHistoryActivity.this,
                        SymptomDiagnosisActivity.class);
                startActivity(intent);
                finish();  // اختياري إذا كنت تريد إزالة شاشة التاريخ من الـ back stack
            });

            // زر مسح السجل مع تأكيد
            btnClearHistory.setOnClickListener(v ->
                    showConfirmationDialog(
                            getString(R.string.confirm_clear),
                            getString(R.string.confirm_clear_message),
                            this::clearHistory
                    )
            );
        });
    }

    private List<String> loadHistory() {
        List<String> list = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        try {
            JSONArray arr = new JSONArray(prefs.getString(
                    "diagnosis_history", "[]"
            ));
            for (int i = 0; i < arr.length(); i++) {
                list.add(arr.getString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private void clearHistory() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        prefs.edit().remove("diagnosis_history").apply();

        history.clear();
        adapter.notifyDataSetChanged();
        Toast.makeText(
                this,
                R.string.clear_history,
                Toast.LENGTH_SHORT
        ).show();
    }

    private void showConfirmationDialog(
            String title,
            String message,
            Runnable onConfirm
    ) {
        // Inflate custom layout
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_confirmation, null, false);

        TextView tvTitle   = dialogView.findViewById(R.id.tvDialogTitle);
        TextView tvMessage = dialogView.findViewById(R.id.tvDialogMessage);
        Button btnCancel   = dialogView.findViewById(R.id.btnCancel);
        Button btnConfirm  = dialogView.findViewById(R.id.btnConfirm);

        tvTitle.setText(title);
        tvMessage.setText(message);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnConfirm.setOnClickListener(v -> {
            dialog.dismiss();
            onConfirm.run();
        });

        dialog.show();
        // لضبط عرض الـ dialog بحد أقصى 80% من عرض الشاشة:
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = (int)(getResources().getDisplayMetrics().widthPixels * 0.8);
            window.setAttributes(params);
        }

    }

    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }
}
