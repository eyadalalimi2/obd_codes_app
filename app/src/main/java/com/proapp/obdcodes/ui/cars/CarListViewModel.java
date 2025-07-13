package com.proapp.obdcodes.ui.cars;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.proapp.obdcodes.network.model.Car;
import com.proapp.obdcodes.repository.CarRepository;
import java.util.List;

/**
 * ViewModel يدير جلب وتحديث قائمة السيارات.
 */
public class CarListViewModel extends AndroidViewModel {

    private final CarRepository repository;
    private final MutableLiveData<List<Car>> userCars = new MutableLiveData<>();
    private final MutableLiveData<Boolean> deleteResult = new MutableLiveData<>();

    public CarListViewModel(@NonNull Application application) {
        super(application);
        repository = new CarRepository(application);
        // حِمّل القائمة عند الإنشاء
        refreshCars();
    }

    /** LiveData لقائمة السيارات التي يراقبها Activity */
    public LiveData<List<Car>> getUserCars() {
        return userCars;
    }

    /** LiveData لنتيجة عملية الحذف */
    public LiveData<Boolean> getDeleteResult() {
        return deleteResult;
    }

    /** يستدعي Repository لجلب أحدث قائمة سيارات */
    public void refreshCars() {
        repository.getUserCars()
                .observeForever(list -> userCars.postValue(list));
    }

    /** يستدعي Repository لحذف السيارة ويضع النتيجة في LiveData */
    public void deleteCar(int carId) {
        repository.deleteCar(carId)
                .observeForever(result -> deleteResult.postValue(result));
    }
}
