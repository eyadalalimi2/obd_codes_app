package com.proapp.obdcodes.ui.visual;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.util.SubscriptionUtils;

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
// ✅ التحقق من صلاحية الميزة
        SubscriptionUtils.hasFeature(this, "VISUAL_COMPONENT_LIBRARY", isAllowed -> {
            if (!isAllowed) {
                Toast.makeText(this, "هذه الميزة متاحة فقط للمشتركين", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            // ✅ السماح بالوصول
            gridView = findViewById(R.id.gridView);
            items = new ArrayList<>();

            items.add(new VisualItem("حساس الأوكسجين", R.drawable.ic_sensor));
            items.add(new VisualItem("كمبيوتر السيارة (ECU)", R.drawable.ic_ecu));
            items.add(new VisualItem("مضخة الوقود", R.drawable.ic_fuel_pump));
            items.add(new VisualItem("فلتر الهواء", R.drawable.ic_air_filter));

            adapter = new VisualLibraryAdapter(this, items);
            gridView.setAdapter(adapter);
        });
    }
    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }

}
