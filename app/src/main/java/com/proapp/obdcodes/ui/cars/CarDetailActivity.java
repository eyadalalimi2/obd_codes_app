// File: app/src/main/java/com/proapp/obdcodes/ui/cars/CarDetailActivity.java
package com.proapp.obdcodes.ui.cars;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.model.Car;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.viewmodel.CarDetailViewModel;

public class CarDetailActivity extends BaseActivity {

    private CarDetailViewModel viewModel;
    private TextView tvBrand, tvModel, tvYear, tvName;
    private ImageView ivCarImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_car_detail);

        tvBrand    = findViewById(R.id.tvBrand);
        tvModel    = findViewById(R.id.tvModel);
        tvYear     = findViewById(R.id.tvYear);
        tvName     = findViewById(R.id.tvName);
        ivCarImage = findViewById(R.id.ivCarImage);

        int carId = getIntent().getIntExtra("CAR_ID", -1);
        viewModel = new ViewModelProvider(this).get(CarDetailViewModel.class);
        viewModel.getSelectedCar().observe(this, this::displayCar);
        viewModel.loadCar(carId);
    }

    private void displayCar(Car car) {
        if (car != null) {
            // ivCarImage: استخدم الصورة الفعلية إن وُجدت
            tvBrand.setText(car.getBrandName());
            tvModel.setText(car.getModelName());
            tvYear.setText(car.getYear());
            tvName.setText(
                    (car.getCarName() != null && !car.getCarName().isEmpty()) ? car.getCarName() : "-"
            );
        }
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return true;
    }
}
