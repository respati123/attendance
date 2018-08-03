package com.example.acer.attandance_free_feature.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.acer.attandance_free_feature.db.entities.Users;

import java.util.List;

@Dao
public interface UsersDao {

    @Query("SELECT * FROM user")
    LiveData<List<Users>> getAllUsers();


    @Insert
    long insert(Users users);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateUsers(Users users);

    @Query("SELECT * FROM user LIMIT 1")
    Users getUsers();

}
