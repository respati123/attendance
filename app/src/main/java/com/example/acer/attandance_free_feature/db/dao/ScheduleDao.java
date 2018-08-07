package com.example.acer.attandance_free_feature.db.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.acer.attandance_free_feature.db.entities.Schedules;

import java.util.List;

@Dao
public interface ScheduleDao {

    @Query("SELECT * FROM schedule ORDER BY sc_date ASC")
    LiveData<List<Schedules>> getAllSchedule();

    @Insert
    void insert(Schedules schedules);

    @Delete
    void delete(Schedules schedules);

    @Update
    void update(Schedules schedules);

}
