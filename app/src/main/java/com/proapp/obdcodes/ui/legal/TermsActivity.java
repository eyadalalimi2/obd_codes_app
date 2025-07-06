package com.proapp.obdcodes.ui.legal;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.proapp.obdcodes.R;

public class TermsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal_text);
        setTitle("شروط الاستخدام");

        TextView tv = findViewById(R.id.legalText);
        tv.setText("شروط الاستخدام:\n\nيُرجى قراءة هذه الشروط بعناية قبل استخدام تطبيق OBD Codes. باستخدامك للتطبيق، فإنك توافق على الالتزام بهذه الشروط. هذا النص تجريبي.");
    }
}
