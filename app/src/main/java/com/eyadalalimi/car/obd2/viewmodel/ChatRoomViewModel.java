package com.eyadalalimi.car.obd2.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.eyadalalimi.car.obd2.local.entity.ChatMessageEntity;
import com.eyadalalimi.car.obd2.repository.ChatMessageRepository;

import java.util.List;

public class ChatRoomViewModel extends AndroidViewModel {
    private final ChatMessageRepository repo;
    private final LiveData<List<ChatMessageEntity>> allMessages;

    public ChatRoomViewModel(@NonNull Application app) {
        super(app);
        repo = new ChatMessageRepository(app);
        allMessages = repo.getAllMessages();
    }

    public LiveData<List<ChatMessageEntity>> getAllMessages() { return allMessages; }
    public void insert(ChatMessageEntity msg) { repo.insert(msg); }
    public void clearAll() { repo.clearAll(); }
    public void clearChatHistory() {
        repo.clearAll(); // التنفيذ الصحيح للحذف
    }
}
