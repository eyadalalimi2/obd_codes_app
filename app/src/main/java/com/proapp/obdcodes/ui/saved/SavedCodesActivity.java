package com.proapp.obdcodes.ui.saved;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.ui.code_search.CodeSearchActivity;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SavedCodesActivity extends BaseActivity
        implements SavedCodesAdapter.Listener {

    private TextView tvSavedCount;
    private Button btnClearAll;
    private RecyclerView rv;
    private List<SavedCode> list;
    private SavedCodesAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_saved_codes);

        // 1. ربط Toolbar بالـ ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 2. تفعيل زر الـ Up (الرجوع) بأمان بعد فحص null
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 3. تهيئة الواجهات
        tvSavedCount = findViewById(R.id.tvSavedCount);
        btnClearAll  = findViewById(R.id.btnClearAll);
        rv           = findViewById(R.id.rvSavedCodes);

        rv.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new SavedCodesAdapter(list, this);
        rv.setAdapter(adapter);

        loadSaved();

        btnClearAll.setOnClickListener(v -> {
            // مسح جميع المحفوظات
            SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
            p.edit().putString("saved_codes", "[]").apply();
            loadSaved();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        // تنفيذ الرجوع عند الضغط على زر Up
        finish();
        return true;
    }

    private void loadSaved() {
        list.clear();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String json = prefs.getString("saved_codes", "[]");
        try {
            JSONArray arr = new JSONArray(json);
            SimpleDateFormat fmt = new SimpleDateFormat("MMM d, yyyy - h:mm a");
            for (int i = 0; i < arr.length(); i++) {
                String entry = arr.getString(i);             // e.g. "P0300|Misfire"
                String[] parts = entry.split("\\|", 2);
                String code = parts[0];
                String title = (parts.length > 1 ? parts[1] : "");
                String desc = "Details for " + code;        // وصف وهمي
                String date = fmt.format(new Date());       // تاريخ التحميل الحالي
                list.add(new SavedCode(date, code, desc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
        tvSavedCount.setText(list.size() + " saved");
    }

    @Override
    public void onDelete(int pos) {
        list.remove(pos);
        saveList();
        adapter.notifyItemRemoved(pos);
        tvSavedCount.setText(list.size() + " saved");
    }

    @Override
    public void onOpen(int pos) {
        SavedCode it = list.get(pos);
        Intent i = new Intent(this, CodeSearchActivity.class);
        i.putExtra("CODE", it.code);
        startActivity(i);
    }

    private void saveList() {
        try {
            JSONArray arr = new JSONArray();
            for (SavedCode it : list) {
                arr.put(it.code + "|" + it.description);
            }
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit().putString("saved_codes", arr.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
