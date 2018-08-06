package com.example.acer.attandance_free_feature.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.acer.attandance_free_feature.db.entities.Absensi;
import com.example.acer.attandance_free_feature.db.entities.AbsensiAndSchedule;
import com.example.acer.attandance_free_feature.db.entities.Schedules;

import java.util.List;

@Dao
public interface AbsensiDao {

    @Query("SELECT * FROM absensi")
    LiveData<List<Absensi>> getAllAbsensi();


    @Insert
    Long Insert(Absensi absensi);

    @Delete
    void Delete(Absensi absensi);


    @Query("SELECT absensi.*, schedule.* FROM absensi INNER JOIN schedule ON absensi.id_schedules = schedule.id_schedule WHERE absensi.ab_date  BETWEEN :from AND :to  ORDER BY ab_date ASC, ab_time ASC")
    LiveData<List<AbsensiAndSchedule>> getAllAbsensiRelationSchedule(String from, String to);

}

