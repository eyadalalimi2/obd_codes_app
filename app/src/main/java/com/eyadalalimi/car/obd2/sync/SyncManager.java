package com.eyadalalimi.car.obd2.sync;

import android.content.Context;
import android.os.AsyncTask;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                    new SaveTask(context, codes, callback).execute();
                } else {
                    callback.onFailure("فشل في تحميل الأكواد من السيرفر");
                }
            }

            @Override
            public void onFailure(Call<List<ObdCode>> call, Throwable t) {
                callback.onFailure("خطأ في الاتصال: " + t.getMessage());
            }
        });
    }

    private static class SaveTask extends AsyncTask<Void, Void, Integer> {
        private final Context context;
        private final List<ObdCode> remoteCodes;
        private final SyncCallback callback;

        public SaveTask(Context context, List<ObdCode> remoteCodes, SyncCallback callback) {
            this.context = context;
            this.remoteCodes = remoteCodes;
            this.callback = callback;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            AppDatabase db = AppDatabase.getInstance(context);
            ObdCodeDao dao = db.obdCodeDao();
            int updatedCount = 0;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            List<ObdCodeEntity> toInsert = new ArrayList<>();

            for (ObdCode remote : remoteCodes) {
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

            return updatedCount;
        }

        @Override
        protected void onPostExecute(Integer count) {
            callback.onSuccess(count);
        }
    }
}
