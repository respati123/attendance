package com.example.acer.attandance_free_feature.db.entities;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;



@Entity(tableName = "user")
public class Users {


    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id_users")
    public int id;

    @ColumnInfo(name = "name")
    public String name;


    @ColumnInfo(name = "username")
    public String username;


    @ColumnInfo(name = "image")
    public String image;


    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

}

