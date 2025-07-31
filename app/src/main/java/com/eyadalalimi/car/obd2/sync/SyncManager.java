package com.eyadalalimi.car.obd2.sync;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.eyadalalimi.car.obd2.local.AppDatabase;
import com.eyadalalimi.car.obd2.local.dao.ObdCodeDao;
import com.eyadalalimi.car.obd2.local.entity.ObdCodeEntity;
import com.eyadalalimi.car.obd2.network.ApiClient;
import com.eyadalalimi.car.obd2.network.ApiService;
import com.eyadalalimi.car.obd2.network.model.ObdCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * مدير المزامنة يستخدم Thread Executor بدلاً من AsyncTask لمعالجة الحفظ في الخلفية.
 */
public class SyncManager {

    public interface SyncCallback {
        void onSuccess(int updatedCount);
        void onFailure(String error);
    }

    public static void downloadCodes(Context context, SyncCallback callback) {
        ApiService api = ApiClient.getApiService(context);
        Call<List<ObdCode>> call = api.getAllCodes();

        call.enqueue(new Callback<List<ObdCode>>() {
            @Override
            public void onResponse(Call<List<ObdCode>> call, Response<List<ObdCode>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ObdCode> codes = response.body();
                    // استخدام ExecutorService لمعالجة الحفظ في الخلفية
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.execute(() -> {
                        AppDatabase db = AppDatabase.getInstance(context);
                        ObdCodeDao dao = db.obdCodeDao();
                        int updatedCount = 0;

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        List<ObdCodeEntity> toInsert = new ArrayList<>();

                        for (ObdCode remote : codes) {
                            String localUpdated = dao.getUpdatedAtForCode(remote.getCode());

                            boolean shouldInsert = false;
                            if (localUpdated == null) {
                                shouldInsert = true;
                            } else {
                                try {
                                    long localTime = sdf.parse(localUpdated).getTime();
                                    long remoteTime = sdf.parse(remote.getUpdatedAt()).getTime();
                                    if (remoteTime > localTime) {
                                        shouldInsert = true;
                                    }
                                } catch (ParseException e) {
                                    shouldInsert = true;
                                }
                            }

                            if (shouldInsert) {
                                ObdCodeEntity entity = ObdCodeEntity.fromObdCode(remote);
                                entity.updatedAt = remote.getUpdatedAt(); // مهم جدًا
                                toInsert.add(entity);
                                updatedCount++;
                            }
                        }

                        if (!toInsert.isEmpty()) {
                            dao.insertAll(toInsert);
                        }

                        int finalCount = updatedCount;
                        // استدعاء الرد على واجهة المستخدم في الـ Main Thread
                        new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(finalCount));
                        executor.shutdown();
                    });
                } else {
                    // في حالة عدم نجاح الاستجابة
                    new Handler(Looper.getMainLooper()).post(
                            () -> callback.onFailure("فشل في تحميل الأكواد من السيرفر")
                    );
                }
            }

            @Override
            public void onFailure(Call<List<ObdCode>> call, Throwable t) {
                // استدعاء الرد على واجهة المستخدم في الـ Main Thread
                new Handler(Looper.getMainLooper()).post(
                        () -> callback.onFailure("خطأ في الاتصال: " + t.getMessage())
                );
            }
        });
    }
}
