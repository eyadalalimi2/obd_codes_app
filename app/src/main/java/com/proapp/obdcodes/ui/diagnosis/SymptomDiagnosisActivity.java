package com.proapp.obdcodes.ui.diagnosis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.proapp.obdcodes.R;
import java.util.*;

public class SymptomDiagnosisActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_symptom_diagnosis);
        setTitle("تشخيص حسب الأعراض");
        etCustomSymptom = findViewById(R.id.etCustomSymptom);
        Button btnBackHome = findViewById(R.id.btnBackHome);
        btnBackHome.setOnClickListener(v -> finish());

        symptomSpinner = findViewById(R.id.symptomSpinner);
        btnDiagnose = findViewById(R.id.btnDiagnose);
        lvResults.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCode = (String) parent.getItemAtPosition(position);

            Intent intent = new Intent(this, com.proapp.obdcodes.ui.code_details.CodeDetailsActivity.class);
            intent.putExtra("code", selectedCode);
            startActivity(intent);
        });

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new ArrayList<>(symptomMap.keySet()));
        symptomSpinner.setAdapter(spinnerAdapter);

        btnDiagnose.setOnClickListener(v -> {
            String selectedSymptom = symptomSpinner.getSelectedItem().toString();
            String customSymptom = etCustomSymptom.getText().toString().trim();

            String[] relatedCodes;

            if (!customSymptom.isEmpty()) {
                relatedCodes = new String[]{"P0001", "P0002"}; // أكواد عامة تجريبية للعروض المدخلة يدويًا
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
            Toast.makeText(this, "فتح الكود: " + selectedCode, Toast.LENGTH_SHORT).show();
            // يمكنك تمرير الكود إلى CodeDetailsActivity هنا
        });
    }
}
