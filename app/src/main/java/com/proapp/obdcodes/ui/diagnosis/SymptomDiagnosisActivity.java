package com.proapp.obdcodes.ui.diagnosis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.ui.code_details.CodeDetailsActivity;

import java.util.*;

public class SymptomDiagnosisActivity extends BaseActivity {

    private Spinner symptomSpinner;
    private Button btnDiagnose;
    private ListView lvResults;
    private EditText etCustomSymptom;

    private final Map<String, String[]> symptomMap = new HashMap<String, String[]>() {{
        put("اهتزاز بالمحرك", new String[]{"P0300", "P0310"});
        put("دخان أسود", new String[]{"P0172", "P2196"});
        put("صوت عالي", new String[]{"P0420", "P0325"});
        put("ضعف التسارع", new String[]{"P0087", "P0299"});
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_symptom_diagnosis);

        // إعداد Toolbar
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etCustomSymptom = findViewById(R.id.etCustomSymptom);
        symptomSpinner  = findViewById(R.id.symptomSpinner);
        btnDiagnose     = findViewById(R.id.btnDiagnose);
        lvResults       = findViewById(R.id.lvResults);

        Button btnBackHome = findViewById(R.id.btnBackHome);
        btnBackHome.setOnClickListener(v -> finish());

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<>(symptomMap.keySet()));
        symptomSpinner.setAdapter(spinnerAdapter);

        btnDiagnose.setOnClickListener(v -> {
            String selectedSymptom = symptomSpinner.getSelectedItem().toString();
            String customSymptom = etCustomSymptom.getText().toString().trim();
            String[] relatedCodes;

            if (!customSymptom.isEmpty()) {
                relatedCodes = new String[]{"P0001", "P0002"};
            } else {
                relatedCodes = symptomMap.get(selectedSymptom);
            }

            if (relatedCodes != null) {
                ArrayAdapter<String> resultAdapter = new ArrayAdapter<>(
                        this, android.R.layout.simple_list_item_1, relatedCodes);
                lvResults.setAdapter(resultAdapter);
            } else {
                Toast.makeText(this, "لا توجد نتائج", Toast.LENGTH_SHORT).show();
            }
        });

        lvResults.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCode = (String) parent.getItemAtPosition(position);
            Intent intent = new Intent(this, CodeDetailsActivity.class);
            intent.putExtra("code", selectedCode);
            startActivity(intent);
        });
    }
    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }

}
