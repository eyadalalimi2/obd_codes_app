package com.proapp.obdcodes.repository;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.proapp.obdcodes.network.ApiClient;
import com.proapp.obdcodes.network.ApiService;
import com.proapp.obdcodes.network.model.ObdCode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodeDetailRepository {

    private final ApiService apiService;

    /**
     * تمرير الـ Context لتمكين الـInterceptor من إضافة التوكن أوتوماتيكياً.
     */
    public CodeDetailRepository(@NonNull Context context) {
        this.apiService = ApiClient.getApiService(context);
    }

    /**
     * جلب تفاصيل الكود بناءً على مسار GET codes/{code}
     */
    public MutableLiveData<ObdCode> fetchCodeDetail(@NonNull String code) {
        MutableLiveData<ObdCode> liveData = new MutableLiveData<>();

        apiService.getCodeDetail(code)
                .enqueue(new Callback<ObdCode>() {
                    @Override
                    public void onResponse(
                            @NonNull Call<ObdCode> call,
                            @NonNull Response<ObdCode> response
                    ) {
                        if (response.isSuccessful() && response.body() != null) {
                            liveData.postValue(response.body());
                        } else {
                            liveData.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(
                            @NonNull Call<ObdCode> call,
                            @NonNull Throwable t
                    ) {
                        liveData.postValue(null);
                    }
                });

        return liveData;
    }
}
