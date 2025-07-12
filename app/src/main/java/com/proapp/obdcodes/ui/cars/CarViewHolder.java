// File: com/proapp/obdcodes/ui/cars/CarViewHolder.java
package com.proapp.obdcodes.ui.cars;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.model.Car;

public class CarViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvCarInfo, tvCarName;
    private final Button btnViewCar, btnDeleteCar;
    private final CarAdapter.OnItemClickListener listener;

    public CarViewHolder(View itemView, CarAdapter.OnItemClickListener listener) {
        super(itemView);
        this.listener = listener;
        tvCarInfo    = itemView.findViewById(R.id.tvCarInfo);
        tvCarName    = itemView.findViewById(R.id.tvCarName);
        btnViewCar   = itemView.findViewById(R.id.btnViewCar);
        btnDeleteCar = itemView.findViewById(R.id.btnDeleteCar);
    }

    public void bind(final Car car) {
        String info = car.getBrandName() + " " + car.getModelName() + " - " + car.getYear();
        tvCarInfo.setText(info);

        if (car.getCarName() != null && !car.getCarName().isEmpty()) {
            tvCarName.setText(car.getCarName());
            tvCarName.setVisibility(View.VISIBLE);
        } else {
            tvCarName.setVisibility(View.GONE);
        }

        btnViewCar.setOnClickListener(v -> listener.onViewClick(car));
        btnDeleteCar.setOnClickListener(v -> listener.onDeleteClick(car));
    }
}
