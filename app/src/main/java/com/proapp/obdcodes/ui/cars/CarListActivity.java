package com.proapp.obdcodes.ui.cars;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.model.Car;
import com.proapp.obdcodes.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class CarListActivity extends BaseActivity {

    private CarListViewModel viewModel;
    private CarAdapter adapter;
    private final List<Car> carList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);

        Toolbar toolbar = findViewById(R.id.toolbarCars);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("سياراتي");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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

        FloatingActionButton fab = findViewById(R.id.fabAddCar);
        fab.setOnClickListener(v ->
                startActivity(new Intent(this, CarFormActivity.class))
        );

        viewModel = new ViewModelProvider(this).get(CarListViewModel.class);
        viewModel.getUserCars().observe(this, cars -> adapter.setCarList(cars));
        viewModel.getDeleteResult().observe(this, success -> {
            if (success) viewModel.refreshCars();
        });
        viewModel.refreshCars();
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
}
