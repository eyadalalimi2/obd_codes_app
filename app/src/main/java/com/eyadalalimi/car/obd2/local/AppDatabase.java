package com.eyadalalimi.car.obd2.local;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.eyadalalimi.car.obd2.local.dao.ChatMessageDao;
import com.eyadalalimi.car.obd2.local.dao.ObdCodeDao;
import com.eyadalalimi.car.obd2.local.entity.ChatMessageEntity;
import com.eyadalalimi.car.obd2.local.entity.ObdCodeEntity;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

@Database(entities = {ChatMessageEntity.class, ObdCodeEntity.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract ChatMessageDao chatMessageDao();
    public abstract ObdCodeDao obdCodeDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            // قراءة مفتاح التشفير من SharedPreferences
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String key = prefs.getString("db_encryption_key", "defaultKey");

            // تحويله إلى bytes لاستخدامه في SQLCipher
            SupportFactory factory = new SupportFactory(SQLiteDatabase.getBytes(key.toCharArray()));

            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "obd_codes_db_encrypted")
                    .openHelperFactory(factory)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
