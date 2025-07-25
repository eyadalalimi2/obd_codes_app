package com.eyadalalimi.car.obd2.ui.history;

import android.app.AlertDialog;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.eyadalalimi.car.obd2.R;

import org.json.JSONArray;

import java.util.List;

public class DiagnosisHistoryAdapter extends BaseAdapter {

    private final Activity context;
    private final List<String> items;

    public DiagnosisHistoryAdapter(Activity context, List<String> items) {
        this.context = context;
        this.items = items;
    }

    @Override public int getCount()               { return items.size(); }
    @Override public Object getItem(int pos)      { return items.get(pos); }
    @Override public long getItemId(int pos)      { return pos; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_diagnosis_history, parent, false);
            vh = new ViewHolder();
            vh.tvCodeTitle = convertView.findViewById(R.id.tvCodeTitle);
            vh.tvDate      = convertView.findViewById(R.id.tvDate);
            vh.btnDelete   = convertView.findViewById(R.id.btnDeleteItem);
            vh.btnShare    = convertView.findViewById(R.id.btnShareItem);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        // تحليل النص المخزن: "P0420 - عنوان - 2025-05-22"
        String entry = items.get(position);
        String[] parts = entry.split(" - ", 3);
        String codeTitle = parts.length >= 2
                ? parts[0] + " - " + parts[1]
                : entry;
        String date = parts.length == 3 ? parts[2] : "";

        vh.tvCodeTitle.setText(codeTitle);
        vh.tvDate.setText(date.isEmpty() ? "" : "التاريخ: " + date);

        // حذف العنصر مع تأكيد
        vh.btnDelete.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("تأكيد الحذف")
                .setMessage("هل أنت متأكد أنك تريد حذف هذا العنصر؟")
                .setPositiveButton("نعم", (dlg, which) -> {
                    items.remove(position);
                    saveHistory();
                    notifyDataSetChanged();
                })
                .setNegativeButton("لا", null)
                .show()
        );

        // مشاركة العنصر
        vh.btnShare.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            String shareText = codeTitle +
                    (date.isEmpty() ? "" : "\nالتاريخ: " + date);
            share.putExtra(Intent.EXTRA_TEXT, shareText);
            context.startActivity(Intent.createChooser(share, "مشاركة"));
        });

        return convertView;
    }

    private void saveHistory() {
        JSONArray arr = new JSONArray();
        for (String s : items) arr.put(s);
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        prefs.edit().putString("diagnosis_history", arr.toString()).apply();
    }

    private static class ViewHolder {
        TextView tvCodeTitle;
        TextView tvDate;
        Button   btnDelete;
        Button   btnShare;
    }
}
