package com.proapp.obdcodes.ui.visual;

import android.os.Bundle;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import com.proapp.obdcodes.R;
import java.util.ArrayList;
import java.util.List;

public class VisualLibraryActivity extends AppCompatActivity {

    private GridView gridView;
    private VisualLibraryAdapter adapter;
    private List<VisualItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_library);
        setTitle("المكتبة المرئية");

        gridView = findViewById(R.id.gridView);
        items = new ArrayList<>();

        items.add(new VisualItem("حساس الأوكسجين", R.drawable.ic_sensor));
        items.add(new VisualItem("كمبيوتر السيارة (ECU)", R.drawable.ic_ecu));
        items.add(new VisualItem("مضخة الوقود", R.drawable.ic_fuel_pump));
        items.add(new VisualItem("فلتر الهواء", R.drawable.ic_air_filter));

        adapter = new VisualLibraryAdapter(this, items);
        gridView.setAdapter(adapter);
    }
}
