package com.example.acer.attandance_free_feature.db.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.acer.attandance_free_feature.db.entities.AbsensiOffice;

import java.util.List;

@Dao
public interface AbsensiOfficeDao {

    @Query("SELECT * From absensi_office")
    LiveData<List<AbsensiOffice>> getAllData();

    @Insert
    void Insert(AbsensiOffice absensiOffice);
}
