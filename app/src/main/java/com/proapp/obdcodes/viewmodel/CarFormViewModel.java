package com.proapp.obdcodes.viewmodel;

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

import java.util.ArrayList;
import java.util.List;

public class CarFormViewModel extends AndroidViewModel {

    private final CarRepository repository;
    private final LiveData<List<Brand>> brands;
    // جعل هذه MutableLiveData نهائية وغير null
    private final MutableLiveData<List<Model>> models = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<String>> years  = new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<AddCarResponse> addResult   = new MutableLiveData<>();
    private final MutableLiveData<UpdateCarsResponse> updateResult = new MutableLiveData<>();

    public CarFormViewModel(@NonNull Application application) {
        super(application);
        repository = new CarRepository(application);
        brands     = repository.getBrands();
        // نعيّن قيمًا أولية فارغة حتى لا تكون null
        models.setValue(new ArrayList<>());
        years.setValue(new ArrayList<>());
    }

    public LiveData<List<Brand>> getBrands() {
        return brands;
    }
    public LiveData<List<Model>> getModels() {
        return models;
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

    public void loadModels(int brandId) {
        repository.getModels(brandId)
                .observeForever(list -> models.postValue(list != null ? list : new ArrayList<>()));
    }
    public void loadYears(int modelId) {
        repository.getYears(modelId)
                .observeForever(list -> years.postValue(list != null ? list : new ArrayList<>()));
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
