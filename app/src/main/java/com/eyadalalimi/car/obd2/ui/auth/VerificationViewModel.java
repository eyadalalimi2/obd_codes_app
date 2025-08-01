package com.eyadalalimi.car.obd2.ui.auth;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.eyadalalimi.car.obd2.network.ApiClient;
import com.eyadalalimi.car.obd2.network.ApiService;
import com.eyadalalimi.car.obd2.network.model.MessageResponse;
import com.eyadalalimi.car.obd2.network.model.VerifyStatusResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationViewModel extends AndroidViewModel {
    private final ApiService api;
    private final MutableLiveData<MessageResponse> sendLink = new MutableLiveData<>();
    private final MutableLiveData<VerifyStatusResponse> status = new MutableLiveData<>();

    public VerificationViewModel(@NonNull Application app) {
        super(app);
        api = ApiClient.getApiService(app);
    }

    public LiveData<MessageResponse> getSendLinkResponse() {
        return sendLink;
    }

    public LiveData<VerifyStatusResponse> getStatusResponse() {
        return status;
    }

    /** إعادة إرسال رابط التفعيل */
    /** إعادة إرسال رابط التفعيل */
    public void sendEmailVerification() {
        api.sendEmailVerification()   // ← يذهب لمسار /email/resend-verification
                .enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            sendLink.setValue(resp.body());
                        } else {
                            sendLink.setValue(new MessageResponse("فشل إرسال رابط التفعيل."));
                        }
                    }
                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {
                        sendLink.setValue(new MessageResponse("خطأ في الاتصال: " + t.getMessage()));
                    }
                });
    }

    // جلب حالة التفعيل الحالية
    public void fetchEmailVerifyStatus() {
        api.getEmailVerifyStatus()
                .enqueue(new Callback<VerifyStatusResponse>() {
                    @Override
                    public void onResponse(Call<VerifyStatusResponse> call,
                                           Response<VerifyStatusResponse> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            status.postValue(resp.body());
                        } else {
                            status.postValue(null);
                        }
                    }
                    @Override
                    public void onFailure(Call<VerifyStatusResponse> call, Throwable t) {
                        status.postValue(null);
                    }
                });
    }
}
