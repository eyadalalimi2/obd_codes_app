package com.proapp.obdcodes.ui.history;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.proapp.obdcodes.R;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;
import android.widget.Button;

public class DiagnosisHistoryActivity extends AppCompatActivity {

    private ListView historyList;
    private Button btnClearHistory;
    private List<String> history;
    private DiagnosisHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis_history);
        setTitle("سجل التشخيص");

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
}