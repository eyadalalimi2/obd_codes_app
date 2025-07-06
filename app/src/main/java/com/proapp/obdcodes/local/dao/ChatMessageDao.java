package com.proapp.obdcodes.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.proapp.obdcodes.local.entity.ChatMessageEntity;

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
