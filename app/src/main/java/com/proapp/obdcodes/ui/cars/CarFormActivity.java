package com.proapp.obdcodes.ui.cars;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.lifecycle.ViewModelProvider;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.model.AddCarResponse;
import com.proapp.obdcodes.network.model.Brand;
import com.proapp.obdcodes.network.model.Model;
import com.proapp.obdcodes.network.model.UpdateCarsResponse;
import com.proapp.obdcodes.ui.base.BaseActivity;

import java.util.List;

public class CarFormActivity extends BaseActivity {

    private CarFormViewModel viewModel;
    private Spinner spBrand, spModel, spYear;
    private EditText etName;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_car_form);

        spBrand = findViewById(R.id.spBrand);
        spModel = findViewById(R.id.spModel);
        spYear  = findViewById(R.id.spYear);
        etName  = findViewById(R.id.etCarName);
        btnSave = findViewById(R.id.btnSaveCar);

        viewModel = new ViewModelProvider(this).get(CarFormViewModel.class);

        viewModel.getBrands().observe(this, this::setupBrands);
        spBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                Brand b = (Brand)p.getItemAtPosition(pos);
                viewModel.loadModels(b.getId());
            }
            @Override public void onNothingSelected(AdapterView<?> p) {}
        });

        viewModel.getModels().observe(this, this::setupModels);
        spModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                Model m = (Model)p.getItemAtPosition(pos);
                viewModel.loadYears(m.getId());
            }
            @Override public void onNothingSelected(AdapterView<?> p) {}
        });

        viewModel.getYears().observe(this, this::setupYears);

        btnSave.setOnClickListener(v -> {
            // 1) تحقق من اختيار الشركة
            Brand b = (Brand) spBrand.getSelectedItem();
            if (b == null) {
                toast("الرجاء اختيار الشركة");
                return;
            }

            // 2) تحقق من اختيار الطراز
            Model m = (Model) spModel.getSelectedItem();
            if (m == null) {
                toast("الرجاء اختيار الطراز");
                return;
            }

            // 3) تحقق من اختيار السنة
            Object yearObj = spYear.getSelectedItem();
            if (yearObj == null) {
                toast("الرجاء اختيار السنة");
                return;
            }
            String y = yearObj.toString().trim();
            if (y.isEmpty()) {
                toast("الرجاء اختيار السنة");
                return;
            }

            // 4) اسم السيارة اختياري
            String n = etName.getText().toString().trim();

            // 5) استدعاء الإضافة
            viewModel.addCar(b.getId(), m.getId(), y, n);
            viewModel.getAddResult().observe(this, this::onAddResult);
        });
    }

    private void setupBrands(List<Brand> list) {
        ArrayAdapter<Brand> ad = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBrand.setAdapter(ad);
    }

    private void setupModels(List<Model> list) {
        ArrayAdapter<Model> ad = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spModel.setAdapter(ad);
    }

    private void setupYears(List<String> list) {
        ArrayAdapter<String> ad = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(ad);
    }

    private void onAddResult(AddCarResponse resp) {
        if (resp != null && resp.getData() != null) {
            toast("تمت إضافة السيارة بنجاح");
            setResult(RESULT_OK);
            finish();
        } else {
            toast("فشل في إضافة السيارة");
        }
    }

    private void onUpdateResult(UpdateCarsResponse resp) {
        if (resp != null) {
            toast("تم تحديث السيارة بنجاح");
            setResult(RESULT_OK);
            finish();
        } else {
            toast("فشل في تحديث السيارة");
        }
    }
    @Override
    protected boolean shouldShowBottomNav() {
        return true;
    }

}
