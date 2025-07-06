package com.proapp.obdcodes.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.proapp.obdcodes.local.dao.ChatMessageDao;
import com.proapp.obdcodes.local.entity.ChatMessageEntity;

@Database(entities = {ChatMessageEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract ChatMessageDao chatMessageDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "obd_codes_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
