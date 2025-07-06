package com.proapp.obdcodes.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.proapp.obdcodes.network.model.ChatMessage;
import com.proapp.obdcodes.network.model.ChatResponse;
import com.proapp.obdcodes.repository.AiChatRepository;

import java.util.ArrayList;
import java.util.List;

public class AiChatViewModel extends AndroidViewModel {
    private final AiChatRepository repository;
    private final MediatorLiveData<ChatResponse> response = new MediatorLiveData<>();
    private LiveData<ChatResponse> currentSource;

    public AiChatViewModel(@NonNull Application application) {
        super(application);
        // تمرير الـ Context إلى الريبو لاستخدام التوكن من SharedPreferences
        this.repository = new AiChatRepository(application);
    }

    /**
     * LiveData للاستجابة (reply, history, error).
     */
    public LiveData<ChatResponse> getResponse() {
        return response;
    }

    /**
     * يرسل رسالة المستخدم للخادم ويحدّث response عند وصول النتيجة.
     * يضمن إزالة الـ observer القديم لتفادي التكرار أو التسريبات.
     *
     * @param message نص الرسالة الجديدة
     */
    public void send(String message) {
        // جمع سجل history الحالي
        List<ChatMessage> history = new ArrayList<>();
        ChatResponse previous = response.getValue();
        if (previous != null && previous.getHistory() != null) {
            history.addAll(previous.getHistory());
        }

        // إزالة المصدر (Observer) القديم إن وُجد
        if (currentSource != null) {
            response.removeSource(currentSource);
        }

        // إنشاء مصدر جديد من الريبو ومراقبته
        currentSource = repository.send(history, message);
        response.addSource(currentSource, chatResponse -> {
            response.setValue(chatResponse);
            // إزالة المصدر بعد استلام أول رد لتجنب التكرار
            response.removeSource(currentSource);
        });
    }


}
