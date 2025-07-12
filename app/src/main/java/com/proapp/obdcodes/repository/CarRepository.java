// File: com/proapp/obdcodes/repository/CarRepository.java
package com.proapp.obdcodes.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.proapp.obdcodes.network.ApiClient;
import com.proapp.obdcodes.network.ApiService;
import com.proapp.obdcodes.network.model.AddCarRequest;
import com.proapp.obdcodes.network.model.AddCarResponse;
import com.proapp.obdcodes.network.model.Brand;
import com.proapp.obdcodes.network.model.BrandsResponse;
import com.proapp.obdcodes.network.model.BrandModelsResponse;
import com.proapp.obdcodes.network.model.DeleteCarResponse;
import com.proapp.obdcodes.network.model.Model;
import com.proapp.obdcodes.network.model.UpdateCarRequest;
import com.proapp.obdcodes.network.model.UpdateCarsResponse;
import com.proapp.obdcodes.network.model.UserCarsResponse;
import com.proapp.obdcodes.network.model.Car;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarRepository {

    private final ApiService apiService;

    public CarRepository(Application application) {
        apiService = ApiClient.getInstance(application).create(ApiService.class);
    }

    public LiveData<List<Car>> getUserCars() {
        MutableLiveData<List<Car>> liveData = new MutableLiveData<>();
        apiService.getUserCars().enqueue(new Callback<UserCarsResponse>() {
            @Override
            public void onResponse(Call<UserCarsResponse> call, Response<UserCarsResponse> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    liveData.postValue(resp.body().getData());
                } else {
                    liveData.postValue(null);
                }
            }
            @Override
            public void onFailure(Call<UserCarsResponse> call, Throwable t) {
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<Boolean> deleteCar(int carId) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        apiService.deleteUserCar(carId).enqueue(new Callback<DeleteCarResponse>() {
            @Override
            public void onResponse(Call<DeleteCarResponse> call, Response<DeleteCarResponse> resp) {
                result.postValue(resp.isSuccessful());
            }
            @Override
            public void onFailure(Call<DeleteCarResponse> call, Throwable t) {
                result.postValue(false);
            }
        });
        return result;
    }

    public LiveData<AddCarResponse> addCar(AddCarRequest request) {
        MutableLiveData<AddCarResponse> liveData = new MutableLiveData<>();
        apiService.addUserCar(request).enqueue(new Callback<AddCarResponse>() {
            @Override
            public void onResponse(Call<AddCarResponse> call, Response<AddCarResponse> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    liveData.postValue(resp.body());
                } else {
                    liveData.postValue(null);
                }
            }
            @Override
            public void onFailure(Call<AddCarResponse> call, Throwable t) {
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<UpdateCarsResponse> updateCar(int carId, UpdateCarRequest request) {
        MutableLiveData<UpdateCarsResponse> liveData = new MutableLiveData<>();
        apiService.updateUserCar(carId, request).enqueue(new Callback<UpdateCarsResponse>() {
            @Override
            public void onResponse(Call<UpdateCarsResponse> call, Response<UpdateCarsResponse> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    liveData.postValue(resp.body());
                } else {
                    liveData.postValue(null);
                }
            }
            @Override
            public void onFailure(Call<UpdateCarsResponse> call, Throwable t) {
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<List<Brand>> getBrands() {
        MutableLiveData<List<Brand>> liveData = new MutableLiveData<>();
        apiService.getBrands().enqueue(new Callback<BrandsResponse>() {
            @Override
            public void onResponse(Call<BrandsResponse> call, Response<BrandsResponse> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    liveData.postValue(resp.body().getData());
                } else {
                    liveData.postValue(null);
                }
            }
            @Override
            public void onFailure(Call<BrandsResponse> call, Throwable t) {
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<List<Model>> getModels(int brandId) {
        MutableLiveData<List<Model>> liveData = new MutableLiveData<>();
        apiService.getBrandModels(brandId).enqueue(new Callback<BrandModelsResponse>() {
            @Override
            public void onResponse(Call<BrandModelsResponse> call, Response<BrandModelsResponse> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    liveData.postValue(resp.body().getModels());
                } else {
                    liveData.postValue(null);
                }
            }
            @Override
            public void onFailure(Call<BrandModelsResponse> call, Throwable t) {
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<List<String>> getYears(int modelId) {
        MutableLiveData<List<String>> liveData = new MutableLiveData<>();
        apiService.getModelYears(modelId).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    liveData.postValue(resp.body());
                } else {
                    liveData.postValue(null);
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                liveData.postValue(null);
            }
        });
        return liveData;
    }
}
