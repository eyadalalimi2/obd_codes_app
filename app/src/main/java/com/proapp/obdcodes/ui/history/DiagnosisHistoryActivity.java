package com.proapp.obdcodes.ui.history;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.utils.SubscriptionUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class DiagnosisHistoryActivity extends BaseActivity {

    private ListView historyList;
    private Button btnClearHistory;
    private List<String> history;
    private DiagnosisHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_diagnosis_history);

        getSupportActionBar().setTitle("سجل التشخيص");

        // ✅ التحقق من توفر الميزة
        SubscriptionUtils.hasFeature(this, "DIAGNOSIS_HISTORY", isAllowed -> {
            if (!isAllowed) {
                Toast.makeText(this, "هذه الميزة متاحة فقط للمشتركين", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            // إذا كانت الميزة مفعلة، تابع التنفيذ العادي
            initHistoryView();
        });
    }

    private void initHistoryView() {
        historyList = findViewById(R.id.lvHistory);
        btnClearHistory = findViewById(R.id.btnClearHistory);

        history = loadHistory();

        adapter = new DiagnosisHistoryAdapter(this, history);
        historyList.setAdapter(adapter);

        btnClearHistory.setOnClickListener(v -> {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().remove("diagnosis_history").apply();

            history.clear();
            adapter.notifyDataSetChanged();

            Toast.makeText(this, "تم مسح السجل بالكامل", Toast.LENGTH_SHORT).show();
        });

        if (history.isEmpty()) {
            Toast.makeText(this, "لا يوجد سجل بعد", Toast.LENGTH_SHORT).show();
        }
    }

    private List<String> loadHistory() {
        List<String> list = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            JSONArray array = new JSONArray(prefs.getString("diagnosis_history", "[]"));
            for (int i = 0; i < array.length(); i++) {
                list.add(array.getString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }
}
