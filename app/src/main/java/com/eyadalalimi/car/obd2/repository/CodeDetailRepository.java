package com.eyadalalimi.car.obd2.repository;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.eyadalalimi.car.obd2.network.ApiClient;
import com.eyadalalimi.car.obd2.network.ApiService;
import com.eyadalalimi.car.obd2.network.model.ObdCode;
import com.eyadalalimi.car.obd2.network.model.ObdCodeTranslation;
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
     * جلب تفاصيل الكود باللغة الإنجليزية فقط (الجدول الرئيسي).
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

    /**
     * جلب تفاصيل الكود بالاعتماد على رمز اللغة:
     * - إذا كانت الإنجليزية → استعلام واحد من الجدول الرئيسي.
     * - إذا كانت لغة أخرى → استعلام من الجدول الرئيسي للحصول على الـ ID،
     *   ثم استعلام ثانٍ من جدول الترجمات ودمج الحقول النصية.
     *
     * @param code     رمز الـ OBD (مثلاً "B0001")
     * @param langCode ISO language code (مثلاً "en", "ar", "fr", "tr")
     */
    public MutableLiveData<ObdCode> fetchCodeDetail(
            @NonNull String code,
            @NonNull String langCode
    ) {
        // للإنجليزية، نعيد نفس الميثود القديم
        if ("en".equalsIgnoreCase(langCode)) {
            return fetchCodeDetail(code);
        }

        MutableLiveData<ObdCode> liveData = new MutableLiveData<>();

        // أولاً: جلب البيانات الأساسية للحصول على الـ ID وبقية الحقول
        apiService.getCodeDetail(code)
                .enqueue(new Callback<ObdCode>() {
                    @Override
                    public void onResponse(
                            @NonNull Call<ObdCode> call,
                            @NonNull Response<ObdCode> response
                    ) {
                        if (response.isSuccessful() && response.body() != null) {
                            ObdCode baseCode = response.body();

                            // ثانياً: جلب الترجمة باستخدام الـ ID واللغة
                            apiService.getCodeTranslation(baseCode.getId(), langCode)
                                    .enqueue(new Callback<ObdCodeTranslation>() {
                                        @Override
                                        public void onResponse(
                                                @NonNull Call<ObdCodeTranslation> call,
                                                @NonNull Response<ObdCodeTranslation> resp2
                                        ) {
                                            if (resp2.isSuccessful() && resp2.body() != null) {
                                                ObdCodeTranslation trans = resp2.body();
                                                // دمج الحقول المترجمة في الكائن الأساسي
                                                baseCode.setTitle(trans.getTitle());
                                                baseCode.setDescription(trans.getDescription());
                                                baseCode.setSymptoms(trans.getSymptoms());
                                                baseCode.setCauses(trans.getCauses());
                                                baseCode.setSolutions(trans.getSolutions());
                                                baseCode.setSeverity(trans.getSeverity());
                                                baseCode.setDiagnosis(trans.getDiagnosis());
                                                liveData.postValue(baseCode);
                                            } else {
                                                // إذا فشل جلب الترجمة، نرجع البيانات الإنجليزية
                                                liveData.postValue(baseCode);
                                            }
                                        }

                                        @Override
                                        public void onFailure(
                                                @NonNull Call<ObdCodeTranslation> call,
                                                @NonNull Throwable t
                                        ) {
                                            liveData.postValue(baseCode);
                                        }
                                    });
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
