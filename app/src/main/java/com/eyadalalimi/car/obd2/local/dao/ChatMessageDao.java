package com.eyadalalimi.car.obd2.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.eyadalalimi.car.obd2.local.entity.ChatMessageEntity;

import java.util.List;

@Dao
public interface ChatMessageDao {
    @Insert
    void insert(ChatMessageEntity message);

    @Query("SELECT * FROM chat_messages ORDER BY id ASC")
    LiveData<List<ChatMessageEntity>> getAll();

    @Query("DELETE FROM chat_messages")
    void clearAll();

}
