package com.proapp.obdcodes.ui.code_search;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.code_details.CodeDetailsActivity;

public class CodeSearchActivity extends AppCompatActivity {

    private EditText etCode;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_search);
        setTitle(getString(R.string.search_code_title));

        etCode = findViewById(R.id.etCode);
        btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(v -> {
            String code = etCode.getText().toString().toUpperCase().trim();
            if (code.length() == 5 && (code.startsWith("P") || code.startsWith("C") ||
                    code.startsWith("B") || code.startsWith("U"))) {
                Intent intent = new Intent(this, CodeDetailsActivity.class);
                intent.putExtra("code", code);
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.invalid_code, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
