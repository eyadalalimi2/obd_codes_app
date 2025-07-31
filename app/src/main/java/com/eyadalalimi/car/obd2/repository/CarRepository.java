// File: app/src/main/java/com/proapp/obdcodes/repository/CarRepository.java
package com.eyadalalimi.car.obd2.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.eyadalalimi.car.obd2.base.ConnectivityInterceptor;
import com.eyadalalimi.car.obd2.network.ApiService;
import com.eyadalalimi.car.obd2.network.model.AddCarRequest;
import com.eyadalalimi.car.obd2.network.model.AddCarResponse;
import com.eyadalalimi.car.obd2.network.model.Brand;
import com.eyadalalimi.car.obd2.network.model.BrandModelsResponse;
import com.eyadalalimi.car.obd2.network.model.BrandsResponse;
import com.eyadalalimi.car.obd2.network.model.Car;
import com.eyadalalimi.car.obd2.network.model.DeleteCarResponse;
import com.eyadalalimi.car.obd2.network.model.Model;
import com.eyadalalimi.car.obd2.network.model.UpdateCarRequest;
import com.eyadalalimi.car.obd2.network.model.UpdateCarsResponse;
import com.eyadalalimi.car.obd2.network.model.UserCarsResponse;
import com.eyadalalimi.car.obd2.network.model.*;
import com.eyadalalimi.car.obd2.network.ApiClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository for Cars feature. Handles network calls and exposes LiveData.
 * تمت إضافة فحص لاستثناء NoConnectivityException رغم أن المعالجة الحالية تقوم فقط بإرجاع null أو false.
 */
public class CarRepository {
    private final ApiService api;

    public CarRepository(Application app) {
        api = ApiClient.getInstance(app).create(ApiService.class);
    }

    public LiveData<List<Car>> getUserCars() {
        MutableLiveData<List<Car>> data = new MutableLiveData<>();
        api.getUserCars().enqueue(new Callback<UserCarsResponse>() {
            @Override public void onResponse(Call<UserCarsResponse> c, Response<UserCarsResponse> r) {
                if (r.isSuccessful() && r.body()!=null) data.postValue(r.body().getData());
            }
            @Override public void onFailure(Call<UserCarsResponse> c, Throwable t) {
                // في حالة انقطاع الاتصال نعيد null، وهو نفس السلوك الحالي
                if (t instanceof ConnectivityInterceptor.NoConnectivityException) {
                    data.postValue(null);
                } else {
                    data.postValue(null);
                }
            }
        });
        return data;
    }

    public LiveData<AddCarResponse> addCar(AddCarRequest req) {
        MutableLiveData<AddCarResponse> data = new MutableLiveData<>();
        api.addUserCar(req).enqueue(new Callback<AddCarResponse>() {
            @Override public void onResponse(Call<AddCarResponse> c, Response<AddCarResponse> r) {
                data.postValue(r.isSuccessful()?r.body():null);
            }
            @Override public void onFailure(Call<AddCarResponse> c, Throwable t) {
                if (t instanceof ConnectivityInterceptor.NoConnectivityException) {
                    data.postValue(null);
                } else {
                    data.postValue(null);
                }
            }
        });
        return data;
    }

    public LiveData<UpdateCarsResponse> updateCar(int id, UpdateCarRequest req) {
        MutableLiveData<UpdateCarsResponse> data = new MutableLiveData<>();
        api.updateUserCar(id, req).enqueue(new Callback<UpdateCarsResponse>() {
            @Override public void onResponse(Call<UpdateCarsResponse> c, Response<UpdateCarsResponse> r) {
                data.postValue(r.isSuccessful()?r.body():null);
            }
            @Override public void onFailure(Call<UpdateCarsResponse> c, Throwable t) {
                if (t instanceof ConnectivityInterceptor.NoConnectivityException) {
                    data.postValue(null);
                } else {
                    data.postValue(null);
                }
            }
        });
        return data;
    }

    public LiveData<Boolean> deleteCar(int id) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        api.deleteUserCar(id).enqueue(new Callback<DeleteCarResponse>() {
            @Override public void onResponse(Call<DeleteCarResponse> c, Response<DeleteCarResponse> r) {
                data.postValue(r.isSuccessful());
            }
            @Override public void onFailure(Call<DeleteCarResponse> c, Throwable t) {
                if (t instanceof ConnectivityInterceptor.NoConnectivityException) {
                    data.postValue(false);
                } else {
                    data.postValue(false);
                }
            }
        });
        return data;
    }

    public LiveData<List<Brand>> getBrands() {
        MutableLiveData<List<Brand>> data = new MutableLiveData<>();
        api.getBrands().enqueue(new Callback<BrandsResponse>() {
            @Override public void onResponse(Call<BrandsResponse> c, Response<BrandsResponse> r) {
                if (r.isSuccessful() && r.body()!=null) data.postValue(r.body().getData());
            }
            @Override public void onFailure(Call<BrandsResponse> c, Throwable t) {
                if (t instanceof ConnectivityInterceptor.NoConnectivityException) {
                    data.postValue(null);
                } else {
                    data.postValue(null);
                }
            }
        });
        return data;
    }

    public LiveData<List<Model>> getModels(int brandId) {
        MutableLiveData<List<Model>> data = new MutableLiveData<>();
        api.getBrandModels(brandId).enqueue(new Callback<BrandModelsResponse>() {
            @Override public void onResponse(Call<BrandModelsResponse> c, Response<BrandModelsResponse> r) {
                if (r.isSuccessful() && r.body()!=null) data.postValue(r.body().getModels());
            }
            @Override public void onFailure(Call<BrandModelsResponse> c, Throwable t) {
                if (t instanceof ConnectivityInterceptor.NoConnectivityException) {
                    data.postValue(null);
                } else {
                    data.postValue(null);
                }
            }
        });
        return data;
    }

    public LiveData<List<String>> getYears(int modelId) {
        MutableLiveData<List<String>> data = new MutableLiveData<>();
        api.getModelYears(modelId).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(response.body());
                } else {
                    data.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                if (t instanceof ConnectivityInterceptor.NoConnectivityException) {
                    data.postValue(null);
                } else {
                    data.postValue(null);
                }
            }
        });
        return data;
    }
}
