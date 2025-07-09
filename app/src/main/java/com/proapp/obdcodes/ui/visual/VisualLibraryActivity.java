package com.proapp.obdcodes.ui.visual;

import android.os.Bundle;
import android.widget.GridView;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class VisualLibraryActivity extends BaseActivity {

    private GridView gridView;
    private VisualLibraryAdapter adapter;
    private List<VisualItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_visual_library); // ✅ استخدام Layout مخصص

        // تهيئة Toolbar ودعمه بزر الرجوع
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("المكتبة المرئية");
        }

        // تهيئة العناصر
        gridView = findViewById(R.id.gridView);
        items = new ArrayList<>();

        // بيانات مؤقتة
        items.add(new VisualItem("حساس الأوكسجين", R.drawable.ic_sensor));
        items.add(new VisualItem("كمبيوتر السيارة (ECU)", R.drawable.ic_ecu));
        items.add(new VisualItem("مضخة الوقود", R.drawable.ic_fuel_pump));
        items.add(new VisualItem("فلتر الهواء", R.drawable.ic_air_filter));

        adapter = new VisualLibraryAdapter(this, items);
        gridView.setAdapter(adapter);
    }
    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }

}
