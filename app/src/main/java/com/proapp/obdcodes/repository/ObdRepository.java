package com.proapp.obdcodes.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.proapp.obdcodes.network.model.CompareResult;
import com.proapp.obdcodes.network.model.ObdCode;
import com.proapp.obdcodes.network.ApiClient;
import com.proapp.obdcodes.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObdRepository {
    private final ApiService api;

    public ObdRepository(Context context) {
        this.api = ApiClient.getApiService(context);
    }

    public LiveData<ObdCode> getCodeDetail(String code) {
        MutableLiveData<ObdCode> liveData = new MutableLiveData<>();
        Call<ObdCode> call = api.getCodeDetail(code);
        call.enqueue(new Callback<ObdCode>() {
            @Override
            public void onResponse(Call<ObdCode> call, Response<ObdCode> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    liveData.postValue(resp.body());
                } else {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ObdCode> call, Throwable t) {
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    /** يسترجع قائمة الأكواد الشائعة */
    public LiveData<List<ObdCode>> getTrendingCodes() {
        MutableLiveData<List<ObdCode>> liveData = new MutableLiveData<>();
        api.getTrendingCodes().enqueue(new Callback<List<ObdCode>>() {
            @Override
            public void onResponse(Call<List<ObdCode>> call, Response<List<ObdCode>> resp) {
                liveData.postValue(resp.isSuccessful() ? resp.body() : null);
            }
            @Override
            public void onFailure(Call<List<ObdCode>> call, Throwable t) {
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    /** يقارن كودين ويرجع النتيجة */
    public LiveData<CompareResult> compareCodes(long id1, long id2) {
        MutableLiveData<CompareResult> liveData = new MutableLiveData<>();
        api.compareCodes(id1, id2).enqueue(new Callback<CompareResult>() {
            @Override
            public void onResponse(Call<CompareResult> call, Response<CompareResult> resp) {
                liveData.postValue(resp.isSuccessful() ? resp.body() : null);
            }
            @Override
            public void onFailure(Call<CompareResult> call, Throwable t) {
                liveData.postValue(null);
            }
        });
        return liveData;
    }

}
