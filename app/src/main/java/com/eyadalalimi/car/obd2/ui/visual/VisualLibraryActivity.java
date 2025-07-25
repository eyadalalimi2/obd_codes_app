package com.eyadalalimi.car.obd2.ui.visual;

import android.os.Bundle;
import android.widget.GridView;

import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.ui.base.BaseActivity;
import com.eyadalalimi.car.obd2.util.SubscriptionUtils;

import java.util.ArrayList;
import java.util.List;

public class VisualLibraryActivity extends BaseActivity {

    private GridView gridView;
    private VisualLibraryAdapter adapter;
    private List<VisualItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // حماية الميزة VISUAL_COMPONENT_LIBRARY قبل تهيئة الواجهة
        SubscriptionUtils.checkFeatureAccess(this, "VISUAL_COMPONENT_LIBRARY", () -> runOnUiThread(() -> {
            setActivityLayout(R.layout.activity_visual_library);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("المكتبة المرئية");
            }

            gridView = findViewById(R.id.gridView);
            items = new ArrayList<>();

            items.add(new VisualItem("حساس الأوكسجين", R.drawable.ic_sensor));
            items.add(new VisualItem("كمبيوتر السيارة (ECU)", R.drawable.ic_ecu));
            items.add(new VisualItem("مضخة الوقود", R.drawable.ic_fuel_pump));
            items.add(new VisualItem("فلتر الهواء", R.drawable.ic_air_filter));

            adapter = new VisualLibraryAdapter(this, items);
            gridView.setAdapter(adapter);
        }));
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }
}
