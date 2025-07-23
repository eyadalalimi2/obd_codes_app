package com.proapp.obdcodes.ui.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.TestActivity;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.ui.code_details.CodeDetailsActivity;
import com.proapp.obdcodes.ui.howitworks.HowItWorksActivity;

import java.util.Locale;

public class HomeActivity extends BaseActivity {
    private AlertDialog exitDialog;
    private TextView[] codeBoxes;
    private TextView tvLanguage;
    private String[] arr1, arr2, arrRest, arrLang, arrLangCodes;
    private int selectedLangIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupExitDialog();
        // حقن واجهة Home داخل الـ base layout
        setActivityLayout(R.layout.activity_home);

        // 1. ربط صناديق الكود
        codeBoxes = new TextView[] {
                findViewById(R.id.box1),
                findViewById(R.id.box2),
                findViewById(R.id.box3),
                findViewById(R.id.box4),
                findViewById(R.id.box5)
        };

        // 2. ربط اختيار اللغة
        LinearLayout languageSelect = findViewById(R.id.languageSelect);
        tvLanguage = findViewById(R.id.tvLanguage);

        // 3. تحميل المصفوفات
        arr1         = getResources().getStringArray(R.array.array_box1);
        arr2         = getResources().getStringArray(R.array.array_box2);
        arrRest      = getResources().getStringArray(R.array.array_box_rest);
        arrLang      = getResources().getStringArray(R.array.array_languages);
        arrLangCodes = getResources().getStringArray(R.array.array_language_codes);

        // تحديد اللغة الافتراضية بناءً على لغة الجهاز
        String deviceLangCode = Locale.getDefault().getLanguage();
        int defaultIndex = 0;
        if ("en".equals(deviceLangCode)) {
            for (int i = 0; i < arrLang.length; i++) {
                if (arrLang[i].equalsIgnoreCase("English")) {
                    defaultIndex = i;
                    break;
                }
            }
        } else if ("ar".equals(deviceLangCode)) {
            for (int i = 0; i < arrLang.length; i++) {
                if (arrLang[i].contains("عرب")) {
                    defaultIndex = i;
                    break;
                }
            }
        }
        selectedLangIndex = defaultIndex;
        tvLanguage.setText(arrLang[defaultIndex]);

        // 4. إعداد النقر لفتح Dialog الشبكة لاختيار حروف الكود
        for (int i = 0; i < codeBoxes.length; i++) {
            final int idx = i;
            codeBoxes[i].setOnClickListener(v -> showPicker(idx));
        }

        // 5. اختيار اللغة
        languageSelect.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.select_language)
                    .setItems(arrLang, (dialog, which) -> {
                        tvLanguage.setText(arrLang[which]);
                        selectedLangIndex = which;
                    })
                    .show();
        });

        // 6. زر البحث
        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(v -> {
            StringBuilder code = new StringBuilder();
            for (TextView tv : codeBoxes) {
                code.append(tv.getText());
            }
            Intent intent = new Intent(this, CodeDetailsActivity.class);
            intent.putExtra("CODE", code.toString());
            intent.putExtra("LANG_CODE", arrLangCodes[selectedLangIndex]);
            startActivity(intent);
        });

        // 7. How It Works
        findViewById(R.id.tvHowItWorks).setOnClickListener(v ->
                startActivity(new Intent(this, HowItWorksActivity.class))
        );

        // 8. مشاركة التطبيق
        findViewById(R.id.btnShare).setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_this_app));
            startActivity(Intent.createChooser(share, getString(R.string.share)));
        });

        // 9. زر لوحة التحكم
        findViewById(R.id.btnGoToDashboard).setOnClickListener(v ->
                startActivity(new Intent(this, TestActivity.class))
        );
    }

    /**
     * يفتح AlertDialog يحوي GridView لاختيار قيمة من arr1/arr2/arrRest
     */
    private void showPicker(int idx) {
        String[] items = (idx == 0 ? arr1 : idx == 1 ? arr2 : arrRest);
        int cols = (idx == 0 ? arr1.length : 5);

        AlertDialog dlg = new AlertDialog.Builder(this).create();
        GridView grid = new GridView(this);
        grid.setNumColumns(cols);
        grid.setHorizontalSpacing(dpToPx(12));
        grid.setVerticalSpacing(dpToPx(12));
        grid.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.item_code_grid,
                R.id.gridItem,
                items
        );
        grid.setAdapter(adapter);

        grid.setOnItemClickListener((parent, view, pos, id) -> {
            codeBoxes[idx].setText(items[pos]);
            dlg.dismiss();
        });

        dlg.setView(grid);
        dlg.show();
    }

    /** تحويل dp إلى px */
    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    private void setupExitDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_exit_confirm, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false);
        exitDialog = builder.create();

        Button btnCancel = view.findViewById(R.id.btn_exit_cancel);
        Button btnConfirm = view.findViewById(R.id.btn_exit_confirm);

        btnCancel.setOnClickListener(v -> exitDialog.dismiss());
        btnConfirm.setOnClickListener(v -> {
            exitDialog.dismiss();
            finishAffinity(); // يغلق كل الـ Activities وينهي التطبيق
        });
    }

    @Override
    public void onBackPressed() {
        // بدل تنفيذ finish() مباشرة، نعرض مربع التأكيد
        if (exitDialog != null && !exitDialog.isShowing()) {
            exitDialog.show();
        } else {
            super.onBackPressed();
        }
    }
}
