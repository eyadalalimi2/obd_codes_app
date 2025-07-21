package com.proapp.obdcodes.ui.saved;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.ui.code_details.CodeDetailsActivity;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class SavedCodesActivity extends BaseActivity implements SavedCodesAdapter.Listener {

    private TextView tvSavedCount;
    private Button btnClearAll;
    private RecyclerView rv;
    private List<SavedCode> list;
    private SavedCodesAdapter adapter;
    private View layoutEmpty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_saved_codes);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            setTitle(R.string.saved_codes_title);
        }

        tvSavedCount = findViewById(R.id.tvSavedCount);
        btnClearAll  = findViewById(R.id.btnClearAll);
        rv           = findViewById(R.id.rvSavedCodes);
        layoutEmpty  = findViewById(R.id.layoutEmpty);

        rv.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new SavedCodesAdapter(list, this);
        rv.setAdapter(adapter);

        loadSaved();

        btnClearAll.setOnClickListener(v -> {
            showDeleteAllDialog();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_saved_codes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            // مثال: ترتيب تنازلي حسب التاريخ
            Collections.sort(list, Comparator.comparing(a -> a.dateTime));
            Collections.reverse(list);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "تم الترتيب حسب الأحدث", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void loadSaved() {
        list.clear();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String json = prefs.getString("saved_codes", "[]");
        try {
            JSONArray arr = new JSONArray(json);
            SimpleDateFormat inFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat outFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm ص"); // التاريخ النهائي

            for (int i = 0; i < arr.length(); i++) {
                try {
                    String entry = arr.getString(i); // "P0300|Misfire|2025-07-21 06:18"
                    String[] parts = entry.split("\\|", 3);
                    String code = parts[0];
                    String desc = (parts.length > 1 ? parts[1] : "");
                    String date = (parts.length > 2 ? parts[2] : "");
                    date = arabicToDecimal(date);

                    String showDate = "";
                    if (!date.isEmpty()) {
                        try {
                            Date d = inFmt.parse(date);
                            showDate = outFmt.format(d);
                        } catch (Exception e) {
                            showDate = date;
                        }
                    } else {
                        showDate = outFmt.format(new Date());
                    }
                    list.add(new SavedCode(showDate, code, desc));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
        updateCountAndEmpty();
    }

    private void updateCountAndEmpty() {
        tvSavedCount.setText(list.size() + " saved");
        if (list.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
            btnClearAll.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            btnClearAll.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDelete(int pos) {
        showDeleteConfirmDialog(pos);
    }

    private void showDeleteConfirmDialog(int pos) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_delete, null, false);
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.DialogStyle)
                .setView(dialogView)
                .create();

        dialogView.findViewById(R.id.btnCancelDelete).setOnClickListener(v -> dialog.dismiss());
        dialogView.findViewById(R.id.btnConfirmDelete).setOnClickListener(v -> {
            list.remove(pos);
            saveList();
            adapter.notifyItemRemoved(pos);
            updateCountAndEmpty();
            dialog.dismiss();
            Snackbar.make(rv, "تم حذف الكود بنجاح", Snackbar.LENGTH_SHORT).show();
        });
        dialog.show();
    }

    private void showDeleteAllDialog() {
        new AlertDialog.Builder(this)
                .setTitle("تأكيد الحذف")
                .setMessage("هل تريد حذف جميع الأكواد المحفوظة؟")
                .setPositiveButton("حذف الكل", (d, w) -> {
                    SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
                    p.edit().putString("saved_codes", "[]").apply();
                    loadSaved();
                    Snackbar.make(rv, "تم حذف جميع الأكواد", Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton("إلغاء", null)
                .show();
    }

    @Override
    public void onOpen(int pos) {
        SavedCode it = list.get(pos);
        Intent i = new Intent(this, CodeDetailsActivity.class);
        i.putExtra("CODE", it.code);
        startActivity(i);
    }

    private void saveList() {
        try {
            JSONArray arr = new JSONArray();
            for (SavedCode it : list) {
                arr.put(it.code + "|" + it.description + "|" + getCurrentDate());
            }
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit().putString("saved_codes", arr.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // تحويل أي نص أرقام عربية إلى أرقام إنجليزية
    public static String arabicToDecimal(String number) {
        if (number == null) return "";
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char c = number.charAt(i);
            if (c >= 0x0660 && c <= 0x0669) { // Arabic-Indic digits
                c -= 0x0660 - '0';
            } else if (c >= 0x06f0 && c <= 0x06f9) { // Extended Arabic-Indic digits
                c -= 0x06f0 - '0';
            }
            chars[i] = c;
        }
        return new String(chars);
    }

    // جلب الوقت الحالي بصيغة التخزين
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date());
    }
}
