// File: com/proapp/obdcodes/ui/cars/CarDetailActivity.java
package com.proapp.obdcodes.ui.cars;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.model.Car;
import com.proapp.obdcodes.ui.base.BaseActivity;

public class CarDetailActivity extends BaseActivity {

    private CarDetailViewModel viewModel;
    private TextView tvBrand, tvModel, tvYear, tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        Toolbar toolbar = findViewById(R.id.toolbarCarDetail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("تفاصيل السيارة");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvBrand = findViewById(R.id.tvBrand);
        tvModel = findViewById(R.id.tvModel);
        tvYear  = findViewById(R.id.tvYear);
        tvName  = findViewById(R.id.tvName);

        int carId = getIntent().getIntExtra("CAR_ID", -1);
        viewModel = new ViewModelProvider(this).get(CarDetailViewModel.class);
        viewModel.getSelectedCar().observe(this, this::displayCar);
        viewModel.loadCar(carId);
    }

    private void displayCar(Car car) {
        if (car != null) {
            tvBrand.setText(car.getBrandName());
            tvModel.setText(car.getModelName());
            tvYear.setText(car.getYear());
            tvName.setText(
                    car.getCarName() != null && !car.getCarName().isEmpty()
                            ? car.getCarName() : "-"
            );
        }
    }
}
