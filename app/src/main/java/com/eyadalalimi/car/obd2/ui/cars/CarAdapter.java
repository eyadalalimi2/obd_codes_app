// File: app/src/main/java/com/proapp/obdcodes/ui/cars/CarAdapter.java
package com.eyadalalimi.car.obd2.ui.cars;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.network.model.Car;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarViewHolder> {
    private List<Car> carList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onViewClick(Car car);
        void onDeleteClick(Car car);
    }

    public CarAdapter(List<Car> carList, OnItemClickListener listener) {
        this.carList = carList;
        this.listener = listener;
    }

    public void setCarList(List<Car> cars) {
        this.carList = cars;
        notifyDataSetChanged();
    }

    @Override
    public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(CarViewHolder holder, int pos) {
        holder.bind(carList.get(pos));
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }
}
