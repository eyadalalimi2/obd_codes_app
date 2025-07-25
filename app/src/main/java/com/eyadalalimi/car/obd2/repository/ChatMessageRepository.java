package com.eyadalalimi.car.obd2.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.eyadalalimi.car.obd2.local.AppDatabase;
import com.eyadalalimi.car.obd2.local.dao.ChatMessageDao;
import com.eyadalalimi.car.obd2.local.entity.ChatMessageEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatMessageRepository {
    private final ChatMessageDao chatDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ChatMessageRepository(Application app) {
        chatDao = AppDatabase.getInstance(app).chatMessageDao();
    }

    public LiveData<List<ChatMessageEntity>> getAllMessages() {
        return chatDao.getAll();
    }

    public void insert(ChatMessageEntity msg) {
        executor.execute(() -> chatDao.insert(msg));
    }

    public void clearAll() {
        executor.execute(chatDao::clearAll);
    }
}
