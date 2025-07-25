package com.eyadalalimi.car.obd2.ui.diagnosis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import com.google.android.material.snackbar.Snackbar;
import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.ui.base.BaseActivity;
import com.eyadalalimi.car.obd2.ui.code_details.CodeDetailsActivity;
import com.eyadalalimi.car.obd2.util.SubscriptionUtils;
import org.json.JSONArray;
import java.text.SimpleDateFormat;
import java.util.*;

public class SymptomDiagnosisActivity extends BaseActivity {

    private Spinner symptomSpinner;
    private Button btnDiagnose;
    private ListView lvResults;
    private EditText etCustomSymptom;
    private Animation shakeAnimation;

    private final Map<String, String[]> symptomMap = new LinkedHashMap<String, String[]>() {{
        put("اهتزاز بالمحرك", new String[]{"P0300", "P0310"});
        put("دخان أسود", new String[]{"P0172", "P2196"});
        put("صوت عالي", new String[]{"P0420", "P0325"});
        put("ضعف التسارع", new String[]{"P0087", "P0299"});
    }};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SubscriptionUtils.checkFeatureAccess(
                this,
                "SYMPTOM_BASED_DIAGNOSIS",
                this::initSymptomUI
        );
    }

    private void initSymptomUI() {
        runOnUiThread(() -> {
            setActivityLayout(R.layout.activity_symptom_diagnosis);
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            symptomSpinner  = findViewById(R.id.symptomSpinner);
            btnDiagnose     = findViewById(R.id.btnDiagnose);
            lvResults       = findViewById(R.id.lvResults);
            etCustomSymptom = findViewById(R.id.etCustomSymptom);
            shakeAnimation  = AnimationUtils.loadAnimation(this, R.anim.shake);

            // تهيئة الـ Spinner
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    new ArrayList<>(symptomMap.keySet())
            );
            symptomSpinner.setAdapter(spinnerAdapter);

            btnDiagnose.setOnClickListener(v -> {
                String selectedSymptom = symptomSpinner.getSelectedItem() != null
                        ? symptomSpinner.getSelectedItem().toString()
                        : "";
                String customSymptom = etCustomSymptom.getText().toString().trim();
                String[] relatedCodes = null;

                // تحقق الإدخال اليدوي
                if (!TextUtils.isEmpty(customSymptom)) {
                    if (customSymptom.length() < 3) {
                        etCustomSymptom.startAnimation(shakeAnimation);
                        showSnackbar("يرجى إدخال وصف صحيح للعرض (3 أحرف على الأقل)", false);
                        return;
                    }
                    relatedCodes = new String[]{"P0001", "P0002"};
                } else if (!TextUtils.isEmpty(selectedSymptom)) {
                    relatedCodes = symptomMap.get(selectedSymptom);
                }

                if (relatedCodes != null && relatedCodes.length > 0) {
                    ArrayAdapter<String> resultAdapter = new ArrayAdapter<>(
                            this, android.R.layout.simple_list_item_1, relatedCodes
                    );
                    lvResults.setAdapter(resultAdapter);

                    // حفظ الحدث في السجل
                    saveDiagnosisHistory(
                            TextUtils.isEmpty(customSymptom) ? selectedSymptom : customSymptom,
                            relatedCodes
                    );

                    showSnackbar("تم جلب النتائج بنجاح", true);
                } else {
                    showSnackbar("لا توجد نتائج مرتبطة بالعرض", false);
                    lvResults.setAdapter(null);
                }
            });

            lvResults.setOnItemClickListener((parent, view, pos, id) -> {
                String selectedCode = (String) parent.getItemAtPosition(pos);
                Intent intent = new Intent(this, CodeDetailsActivity.class);
                intent.putExtra("CODE", selectedCode);
                startActivity(intent);
            });
        });
    }

    private void saveDiagnosisHistory(String symptom, String[] codes) {
        SharedPreferences prefs = androidx.preference.PreferenceManager
                .getDefaultSharedPreferences(this);
        String raw = prefs.getString("diagnosis_history", "[]");
        JSONArray arr;
        try {
            arr = new JSONArray(raw);
        } catch (Exception e) {
            arr = new JSONArray();
        }
        String codesCsv = TextUtils.join(",", codes);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());
        String entry = symptom + " - " + codesCsv + " - " + date;
        arr.put(entry);
        prefs.edit().putString("diagnosis_history", arr.toString()).apply();
    }

    private void showSnackbar(String message, boolean isSuccess) {
        View root = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(root, message, Snackbar.LENGTH_SHORT);
        View  view     = snackbar.getView();
        TextView text  = view.findViewById(
                com.google.android.material.R.id.snackbar_text
        );
        text.setTypeface(
                ResourcesCompat.getFont(this, R.font.tajawal_medium)
        );
        if (isSuccess) {
            view.setBackgroundColor(0xFF43A047);
            text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);
        } else {
            view.setBackgroundColor(0xFFD32F2F);
            text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_info, 0, 0, 0);
        }
        snackbar.show();
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }
}
