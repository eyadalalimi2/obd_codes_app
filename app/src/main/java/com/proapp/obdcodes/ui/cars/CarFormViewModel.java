// File: com/proapp/obdcodes/ui/cars/CarFormViewModel.java
package com.proapp.obdcodes.ui.cars;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.proapp.obdcodes.network.model.AddCarRequest;
import com.proapp.obdcodes.network.model.AddCarResponse;
import com.proapp.obdcodes.network.model.Brand;
import com.proapp.obdcodes.network.model.Model;
import com.proapp.obdcodes.network.model.UpdateCarRequest;
import com.proapp.obdcodes.network.model.UpdateCarsResponse;
import com.proapp.obdcodes.repository.CarRepository;

import java.util.List;

public class CarFormViewModel extends AndroidViewModel {

    private final CarRepository repository;
    private final LiveData<List<Brand>> brands;
    private LiveData<List<Model>> models;
    private LiveData<List<String>> years;

    private final MutableLiveData<AddCarResponse> addResult = new MutableLiveData<>();
    private final MutableLiveData<UpdateCarsResponse> updateResult = new MutableLiveData<>();

    public CarFormViewModel(@NonNull Application application) {
        super(application);
        repository = new CarRepository(application);
        brands = repository.getBrands();
    }

    public LiveData<List<Brand>> getBrands() {
        return brands;
    }
    public void loadModels(int brandId) {
        models = repository.getModels(brandId);
    }
    public LiveData<List<Model>> getModels() {
        return models;
    }
    public void loadYears(int modelId) {
        years = repository.getYears(modelId);
    }
    public LiveData<List<String>> getYears() {
        return years;
    }

    public LiveData<AddCarResponse> getAddResult() {
        return addResult;
    }
    public LiveData<UpdateCarsResponse> getUpdateResult() {
        return updateResult;
    }

    public void addCar(int brandId, int modelId, String year, String name) {
        repository.addCar(new AddCarRequest(brandId, modelId, year, name))
                .observeForever(resp -> addResult.postValue(resp));
    }
    public void updateCar(int carId, int brandId, int modelId, String year, String name) {
        repository.updateCar(carId, new UpdateCarRequest(brandId, modelId, year, name))
                .observeForever(resp -> updateResult.postValue(resp));
    }
}
