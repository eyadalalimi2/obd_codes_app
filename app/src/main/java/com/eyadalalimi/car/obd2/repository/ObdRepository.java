package com.eyadalalimi.car.obd2.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eyadalalimi.car.obd2.local.AppDatabase;
import com.eyadalalimi.car.obd2.local.dao.ObdCodeDao;
import com.eyadalalimi.car.obd2.local.entity.ObdCodeEntity;
import com.eyadalalimi.car.obd2.network.model.CompareResult;
import com.eyadalalimi.car.obd2.network.model.ObdCode;
import com.eyadalalimi.car.obd2.network.ApiClient;
import com.eyadalalimi.car.obd2.network.ApiService;
import com.eyadalalimi.car.obd2.utils.NetworkUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObdRepository {
    private final ApiService api;
    private final ObdCodeDao localDao;
    private final Context context;

    public ObdRepository(Context context) {
        this.context = context.getApplicationContext();
        this.api = ApiClient.getApiService(this.context);
        this.localDao = AppDatabase.getInstance(this.context).obdCodeDao();
    }

    /** ✅ الحصول على تفاصيل كود واحد */
    public LiveData<ObdCode> getCodeDetail(String code) {
        MutableLiveData<ObdCode> liveData = new MutableLiveData<>();

        if (NetworkUtil.isConnected(context)) {
            api.getCodeDetail(code).enqueue(new Callback<ObdCode>() {
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
        } else {
            // وضع أوفلاين: جلب من Room
            Executors.newSingleThreadExecutor().execute(() -> {
                ObdCodeEntity entity = localDao.findByCode(code);
                if (entity != null) {
                    ObdCode offlineCode = entity.toObdCode(); // نفذ هذه الدالة داخل ObdCodeEntity
                    liveData.postValue(offlineCode);
                } else {
                    liveData.postValue(null);
                }
            });
        }

        return liveData;
    }

    /** ✅ الأكواد الشائعة */
    public LiveData<List<ObdCode>> getTrendingCodes() {
        MutableLiveData<List<ObdCode>> liveData = new MutableLiveData<>();

        if (NetworkUtil.isConnected(context)) {
            api.getTrendingCodes().enqueue(new Callback<List<ObdCode>>() {
                @Override
                public void onResponse(Call<List<ObdCode>> call, Response<List<ObdCode>> resp) {
                    if (resp.isSuccessful() && resp.body() != null) {
                        // حفظ الأكواد في قاعدة البيانات المحلية
                        Executors.newSingleThreadExecutor().execute(() -> {
                            List<ObdCodeEntity> entities = new ArrayList<>();
                            for (ObdCode code : resp.body()) {
                                entities.add(ObdCodeEntity.fromObdCode(code));
                            }
                            localDao.insertAll(entities);
                        });
                        liveData.postValue(resp.body());
                    } else {
                        liveData.postValue(null);
                    }
                }

                @Override
                public void onFailure(Call<List<ObdCode>> call, Throwable t) {
                    liveData.postValue(null);
                }
            });
        } else {
            // في حالة أوفلاين: استرجاع من Room
            Executors.newSingleThreadExecutor().execute(() -> {
                List<ObdCodeEntity> localList = localDao.getAllCodesRaw(); // getAllCodes() تعيد LiveData، نستخدم نسخة RAW
                List<ObdCode> result = new ArrayList<>();
                for (ObdCodeEntity entity : localList) {
                    result.add(entity.toObdCode());
                }
                liveData.postValue(result);
            });
        }

        return liveData;
    }

    /** ✅ المقارنة بين كودين (فقط أونلاين) */
    public LiveData<CompareResult> compareCodes(long id1, long id2) {
        MutableLiveData<CompareResult> liveData = new MutableLiveData<>();

        if (NetworkUtil.isConnected(context)) {
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
        } else {
            liveData.postValue(null); // غير متاح بدون إنترنت
        }

        return liveData;
    }
}
