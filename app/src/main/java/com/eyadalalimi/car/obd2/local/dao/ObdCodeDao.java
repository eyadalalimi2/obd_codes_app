package com.eyadalalimi.car.obd2.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.eyadalalimi.car.obd2.local.entity.ObdCodeEntity;

import java.util.List;

@Dao
public interface ObdCodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ObdCodeEntity> codes);

    @Query("SELECT * FROM obd_codes ORDER BY code ASC")
    List<ObdCodeEntity> getAllCodes();

    @Query("SELECT * FROM obd_codes WHERE code = :code LIMIT 1")
    ObdCodeEntity getCodeByCode(String code);

    @Query("DELETE FROM obd_codes")
    void deleteAll();
    @Query("SELECT * FROM obd_codes WHERE code = :code LIMIT 1")
    ObdCodeEntity findByCode(String code);

    @Query("SELECT * FROM obd_codes")
    List<ObdCodeEntity> getAllCodesRaw();
    @Query("SELECT updatedAt FROM obd_codes WHERE code = :code LIMIT 1")
    String getUpdatedAtForCode(String code);

}
