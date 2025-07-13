package com.proapp.obdcodes.ui.cars;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.model.Car;
import com.proapp.obdcodes.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity تعرض قائمة السيارات وتدير عمليات العرض والحذف.
 */
public class CarListActivity extends BaseActivity {

    private CarListViewModel viewModel;
    private CarAdapter adapter;
    private final List<Car> carList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_car_list);

        // إعداد RecyclerView و Adapter
        RecyclerView rv = findViewById(R.id.rvCars);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CarAdapter(carList, new CarAdapter.OnItemClickListener() {
            @Override
            public void onViewClick(Car car) {
                startActivity(new Intent(CarListActivity.this, CarDetailActivity.class)
                        .putExtra("CAR_ID", car.getId()));
            }
            @Override
            public void onDeleteClick(Car car) {
                showDeleteDialog(car);
            }
        });
        rv.setAdapter(adapter);

        // زر الإضافة
        FloatingActionButton fab = findViewById(R.id.fabAddCar);
        fab.setOnClickListener(v ->
                startActivity(new Intent(this, CarFormActivity.class))
        );

        // ViewModel واستقبال البيانات
        viewModel = new ViewModelProvider(this).get(CarListViewModel.class);

        // راقب تغييرات قائمة السيارات (غير فارغة أبداً)
        viewModel.getUserCars().observe(this, cars -> {
            if (cars != null) {
                adapter.setCarList(cars);
            }
        });

        // راقب نتائج الحذف وأعد تحميل القائمة عند النجاح
        viewModel.getDeleteResult().observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                viewModel.refreshCars();
            }
        });
    }

    private void showDeleteDialog(Car car) {
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_confirm_delete_car, null);
        Button btnCancel = dialogView.findViewById(R.id.btnCancelDelete);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirmDelete);

        Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogView);
        dialog.setCancelable(true);
        dialog.show();

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnConfirm.setOnClickListener(v -> {
            dialog.dismiss();
            viewModel.deleteCar(car.getId());
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        viewModel.refreshCars();
    }
    @Override
    protected boolean shouldShowBottomNav() {
        return true;
    }
}
