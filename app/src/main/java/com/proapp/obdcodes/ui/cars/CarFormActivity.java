// File: com/proapp/obdcodes/ui/cars/CarFormActivity.java
package com.proapp.obdcodes.ui.cars;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;
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
    private Integer editingId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_form);

        Toolbar toolbar = findViewById(R.id.toolbarCarForm);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("إضافة / تعديل سيارة");
        }

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
            @Override public void onNothingSelected(AdapterView<?> p) { }
        });

        viewModel.getModels().observe(this, this::setupModels);
        spModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                Model m = (Model)p.getItemAtPosition(pos);
                viewModel.loadYears(m.getId());
            }
            @Override public void onNothingSelected(AdapterView<?> p) { }
        });

        viewModel.getYears().observe(this, this::setupYears);

        btnSave.setOnClickListener(v -> {
            Brand  b = (Brand) spBrand.getSelectedItem();
            Model  m = (Model) spModel.getSelectedItem();
            String y = (String) spYear.getSelectedItem();
            String n = etName.getText().toString().trim();

            if (editingId==null) {
                viewModel.addCar(b.getId(), m.getId(), y, n);
                viewModel.getAddResult().observe(this, this::onAddResult);
            } else {
                viewModel.updateCar(editingId, b.getId(), m.getId(), y, n);
                viewModel.getUpdateResult().observe(this, this::onUpdateResult);
            }
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
        if (resp!=null && resp.getData()!=null) finish();
        else showToast("فشل في إضافة السيارة");
    }
    private void onUpdateResult(UpdateCarsResponse resp) {
        if (resp!=null) finish();
        else showToast("فشل في تحديث السيارة");
    }
}
