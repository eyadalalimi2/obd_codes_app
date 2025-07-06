package com.proapp.obdcodes.ui.history;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.proapp.obdcodes.R;
import org.json.JSONArray;
import java.util.List;

public class DiagnosisHistoryAdapter extends BaseAdapter {

    private final Activity context;
    private final List<String> items;

    public DiagnosisHistoryAdapter(Activity context, List<String> items) {
        this.context = context;
        this.items = items;
    }

    @Override public int getCount() { return items.size(); }
    @Override public Object getItem(int i) { return items.get(i); }
    @Override public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.item_diagnosis_history, parent, false);

        TextView tvCodeTitle = view.findViewById(R.id.tvCodeTitle);
        TextView tvDate = view.findViewById(R.id.tvDate);
        Button btnDelete = view.findViewById(R.id.btnDeleteItem);

        String fullEntry = items.get(i); // P0420 - عنوان - 2025-05-22
        String[] parts = fullEntry.split(" - ");

        if (parts.length >= 3) {
            tvCodeTitle.setText(parts[0] + " - " + parts[1]);
            tvDate.setText("التاريخ: " + parts[2]);
        } else {
            tvCodeTitle.setText(fullEntry);
            tvDate.setText("");
        }

        btnDelete.setOnClickListener(v -> {
            // حذف من SharedPreferences
            items.remove(i);
            saveUpdatedList();
            notifyDataSetChanged();
        });

        return view;
    }

    private void saveUpdatedList() {
        JSONArray array = new JSONArray();
        for (String item : items) {
            array.put(item);
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("diagnosis_history", array.toString()).apply();
    }
}
