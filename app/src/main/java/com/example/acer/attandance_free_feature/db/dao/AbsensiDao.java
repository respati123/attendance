package com.example.acer.attandance_free_feature.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.acer.attandance_free_feature.db.entities.Absensi;

import java.util.List;

@Dao
public interface AbsensiDao {

    @Query("SELECT * FROM absensi")
    LiveData<List<Absensi>> getAllAbsensi();


    @Insert
    Long Insert(Absensi absensi);

    @Delete
    void Delete(Absensi absensi);

}
