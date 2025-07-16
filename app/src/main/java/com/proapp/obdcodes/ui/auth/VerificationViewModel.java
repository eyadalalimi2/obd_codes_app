package com.proapp.obdcodes.ui.auth;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.proapp.obdcodes.network.ApiClient;
import com.proapp.obdcodes.network.ApiService;
import com.proapp.obdcodes.network.model.MessageResponse;
import com.proapp.obdcodes.network.model.VerifyStatusResponse;
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
    public void sendEmailVerification() {
        api.sendEmailVerification()
                .enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> c, Response<MessageResponse> r) {
                        if (r.isSuccessful() && r.body() != null)
                            sendLink.setValue(r.body());
                        else {
                            MessageResponse error = new MessageResponse("فشل إرسال رابط التفعيل.");
                            sendLink.setValue(error);
                        }
                    }
                    @Override
                    public void onFailure(Call<MessageResponse> c, Throwable t) {
                        sendLink.setValue(new MessageResponse("خطأ في الاتصال: " + t.getMessage()));
                    }
                });
    }

    /** جلب حالة التحقق (مفعل / غير مفعل) */
    public void getEmailVerifyStatus() {
        api.getEmailVerifyStatus()
                .enqueue(new Callback<VerifyStatusResponse>() {
                    @Override
                    public void onResponse(Call<VerifyStatusResponse> c, Response<VerifyStatusResponse> r) {
                        if (r.isSuccessful() && r.body() != null)
                            status.setValue(r.body());
                        else
                            status.setValue(null);
                    }
                    @Override
                    public void onFailure(Call<VerifyStatusResponse> c, Throwable t) {
                        status.setValue(null);
                    }
                });
    }
}
