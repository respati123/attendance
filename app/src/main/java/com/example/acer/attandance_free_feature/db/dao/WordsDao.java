package com.example.acer.attandance_free_feature.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.acer.attandance_free_feature.db.entities.Words;

import java.util.List;

@Dao
public interface WordsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Words word);

    @Delete
    void delete(Words word);
    @Query("DELETE FROM words")
    void deleteAll();

    @Update
    void update(Words word);

    @Query("SELECT * FROM words ORDER BY word ASC")
    LiveData<List<Words>> getAllWords();
}
