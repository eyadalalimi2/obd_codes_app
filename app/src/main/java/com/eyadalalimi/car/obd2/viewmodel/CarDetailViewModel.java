// File: app/src/main/java/com/proapp/obdcodes/ui/cars/CarDetailViewModel.java
package com.eyadalalimi.car.obd2.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import com.eyadalalimi.car.obd2.network.model.Car;
import com.eyadalalimi.car.obd2.repository.CarRepository;
import java.util.List;

public class CarDetailViewModel extends AndroidViewModel {
    private final CarRepository repository;
    private final MediatorLiveData<Car> selectedCar = new MediatorLiveData<>();
    private final LiveData<List<Car>> allCars;

    public CarDetailViewModel(@NonNull Application app) {
        super(app);
        repository = new CarRepository(app);
        allCars = repository.getUserCars();
    }

    public LiveData<Car> getSelectedCar() {
        return selectedCar;
    }

    public void loadCar(int carId) {
        selectedCar.addSource(allCars, cars -> {
            if (cars!=null) {
                for (Car c: cars) {
                    if (c.getId()==carId) {
                        selectedCar.setValue(c);
                        break;
                    }
                }
            }
        });
    }
}
