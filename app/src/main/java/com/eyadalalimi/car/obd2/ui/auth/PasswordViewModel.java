package com.eyadalalimi.car.obd2.ui.auth;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.eyadalalimi.car.obd2.network.ApiClient;
import com.eyadalalimi.car.obd2.network.ApiService;
import com.eyadalalimi.car.obd2.network.model.ForgotPasswordRequest;
import com.eyadalalimi.car.obd2.network.model.MessageResponse;
import com.eyadalalimi.car.obd2.network.model.ResetPasswordRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordViewModel extends AndroidViewModel {
    private final ApiService api;
    private final MutableLiveData<Result> forgotResult = new MutableLiveData<>();
    private final MutableLiveData<Result> resetResult = new MutableLiveData<>();

    public static class Result {
        private final boolean ok;
        private final String message;
        public Result(boolean ok, String message) { this.ok = ok; this.message = message; }
        public boolean isOk() { return ok; }
        public String getMessage() { return message; }
    }

    public PasswordViewModel(@NonNull Application app) {
        super(app);
        api = ApiClient.getApiService(app);
    }

    public LiveData<Result> getForgotResult() { return forgotResult; }
    public LiveData<Result> getResetResult() { return resetResult; }

    /** إرسال رابط إعادة تعيين */
    public void forgotPassword(String email) {
        api.forgotPassword(new ForgotPasswordRequest(email))
                .enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> c, Response<MessageResponse> r) {
                        if (r.isSuccessful() && r.body() != null)
                            forgotResult.setValue(new Result(true, r.body().getMessage()));
                        else {
                            String msg = "فشل في الإرسال.";
                            if (r.errorBody() != null) try { msg = r.errorBody().string(); } catch (Exception ignored) {}
                            forgotResult.setValue(new Result(false, msg));
                        }
                    }
                    @Override
                    public void onFailure(Call<MessageResponse> c, Throwable t) {
                        forgotResult.setValue(new Result(false, "خطأ في الاتصال: " + t.getMessage()));
                    }
                });
    }

    /** إعادة تعيين كلمة المرور */
    public void resetPassword(String email, String token, String password, String confirm) {
        api.resetPassword(new ResetPasswordRequest(email, token, password, confirm))
                .enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> c, Response<MessageResponse> r) {
                        if (r.isSuccessful() && r.body() != null)
                            resetResult.setValue(new Result(true, r.body().getMessage()));
                        else {
                            String msg = "فشل إعادة التعيين.";
                            if (r.errorBody() != null) try { msg = r.errorBody().string(); } catch (Exception ignored) {}
                            resetResult.setValue(new Result(false, msg));
                        }
                    }
                    @Override
                    public void onFailure(Call<MessageResponse> c, Throwable t) {
                        resetResult.setValue(new Result(false, "خطأ في الاتصال: " + t.getMessage()));
                    }
                });
    }
}
