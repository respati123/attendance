package com.example.acer.attandance_free_feature.db.entities;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "absensi", foreignKeys =
        {@ForeignKey(entity = Users.class,
                parentColumns = {"id_users"},
                childColumns = {"id_user"},
                onDelete = CASCADE)})

public class Absensi {


//    public Absensi(int id, int id_user, String time, Long lat, Long lon, String image, String type){
//        this.id = id;
//        this.id_user = id_user;
//        this.time = time;
//        this.lat = lat;
//        this.lon = lon;
//        this.image = image;
//        this.type = type;
//    }
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "id_user")
    public int id_user;

    @ColumnInfo(name = "time")
    public String time;

    @ColumnInfo(name = "lat")
    public Double lat;

    @ColumnInfo(name = "lon")
    public Double lon;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "type")
    public String type;


    @NonNull
    public int getId() {
        return id;
    }


    public String getImage() {
        return image;
    }

    public int getId_user() {
        return id_user;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }
}
