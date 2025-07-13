// File: app/src/main/java/com/proapp/obdcodes/ui/cars/CarViewHolder.java
package com.proapp.obdcodes.ui.cars;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.model.Car;

public class CarViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvCarInfo, tvCarName;
    private final Button btnViewCar, btnDeleteCar;
    private final CarAdapter.OnItemClickListener listener;

    public CarViewHolder(@NonNull View itemView, CarAdapter.OnItemClickListener listener) {
        super(itemView);
        this.listener = listener;
        tvCarInfo    = itemView.findViewById(R.id.tvCarInfo);
        tvCarName    = itemView.findViewById(R.id.tvCarName);
        btnViewCar   = itemView.findViewById(R.id.btnViewCar);
        btnDeleteCar = itemView.findViewById(R.id.btnDeleteCar);
    }

    public void bind(@NonNull final Car car) {
        // بناء نص المعلومات مع حماية من null
        String brandName = car.getBrandName() != null ? car.getBrandName() : "";
        String modelName = car.getModelName() != null ? car.getModelName() : "";
        String year      = car.getYear()      != null ? car.getYear()      : "";
        String info = brandName + " " + modelName + " - " + year;
        tvCarInfo.setText(info);

        // عرض أو إخفاء اسم السيارة مع حماية من null
        String carName = car.getCarName();
        if (carName != null && !carName.isEmpty()) {
            tvCarName.setText(carName);
            tvCarName.setVisibility(View.VISIBLE);
        } else {
            tvCarName.setVisibility(View.GONE);
        }

        // التعامل مع الأزرار
        btnViewCar.setOnClickListener(v -> listener.onViewClick(car));
        btnDeleteCar.setOnClickListener(v -> listener.onDeleteClick(car));
    }
}
