package com.proapp.obdcodes.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.proapp.obdcodes.network.ApiClient;
import com.proapp.obdcodes.network.ApiService;
import com.proapp.obdcodes.network.model.ChatMessage;
import com.proapp.obdcodes.network.model.ChatRequest;
import com.proapp.obdcodes.network.model.ChatResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AiChatRepository {
    private final ApiService api;

    public AiChatRepository(Context context) {
        // الحصول على ApiService مُهيّأ بالـ token من SharedPreferences
        this.api = ApiClient.getApiService(context);
    }

    /**
     * يرسل رسالة إلى خادم AI ويعيد LiveData تحتوي على الاستجابة.
     *
     * @param history سجل المحادثة السابق (قد يكون فارغًا).
     * @param message نص الرسالة الجديدة من المستخدم.
     * @return LiveData لـ ChatResponse، تحوي reply أو error وسجل history المحدث.
     */
    public LiveData<ChatResponse> send(List<ChatMessage> history, String message) {
        MutableLiveData<ChatResponse> liveData = new MutableLiveData<>();

        ChatRequest request = new ChatRequest(history, message);
        api.chat(request).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                ChatResponse cr;
                if (response.isSuccessful() && response.body() != null) {
                    cr = response.body();
                } else {
                    cr = new ChatResponse();
                    cr.setError("خطأ في الخادم: " + response.code());
                    // حافظ على سجل الدردشة السابق حتى لا يفقد المستخدم سياق المحادثة
                    cr.setHistory(history);
                }
                liveData.postValue(cr);
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                ChatResponse cr = new ChatResponse();
                cr.setError("فشل الاتصال: " + t.getMessage());
                // حافظ على سجل الدردشة السابق أيضاً عند الفشل
                cr.setHistory(history);
                liveData.postValue(cr);
            }
        });

        return liveData;
    }
}
