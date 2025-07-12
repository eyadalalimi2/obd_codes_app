// File: com/proapp/obdcodes/ui/cars/CarListViewModel.java
package com.proapp.obdcodes.ui.cars;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.proapp.obdcodes.network.model.Car;
import com.proapp.obdcodes.repository.CarRepository;

import java.util.List;

public class CarListViewModel extends AndroidViewModel {

    private final CarRepository repository;
    private LiveData<List<Car>> userCars;
    private LiveData<Boolean> deleteResult;

    public CarListViewModel(@NonNull Application application) {
        super(application);
        repository = new CarRepository(application);
        refreshCars();
    }

    public LiveData<List<Car>> getUserCars() {
        return userCars;
    }

    public LiveData<Boolean> getDeleteResult() {
        return deleteResult;
    }

    public void refreshCars() {
        userCars = repository.getUserCars();
    }

    public void deleteCar(int carId) {
        deleteResult = repository.deleteCar(carId);
    }
}
