// File: com/proapp/obdcodes/ui/cars/CarAdapter.java
package com.proapp.obdcodes.ui.cars;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.model.Car;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarViewHolder> {

    public interface OnItemClickListener {
        void onViewClick(Car car);
        void onDeleteClick(Car car);
    }

    private List<Car> carList;
    private final OnItemClickListener listener;

    public CarAdapter(List<Car> carList, OnItemClickListener listener) {
        this.carList = carList;
        this.listener = listener;
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        holder.bind(carList.get(position));
    }

    @Override
    public int getItemCount() {
        return carList != null ? carList.size() : 0;
    }
}
