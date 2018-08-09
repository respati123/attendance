package com.example.acer.attandance_free_feature.db.entities;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.text.format.Time;

import java.util.ArrayList;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "absensi", foreignKeys =
        {@ForeignKey(entity = Users.class,
                parentColumns = {"id_users"},
                childColumns = {"ab_id_user"},
                onDelete = CASCADE),
        @ForeignKey(entity = Schedules.class,
                parentColumns = {"id_schedule"},
                childColumns = {"id_schedules"},
                onDelete = CASCADE)})

public class Absensi {


    public Absensi(int id_user, String date, String time, Double lat, Double lon, String image, String type, int check){
        this.id_user = id_user;
        this.time = time;
        this.date = date;
        this.lat = lat;
        this.lon = lon;
        this.image = image;
        this.type = type;
        this.check = check;
    }
    public Absensi(){}
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "ab_id_user")
    public int id_user;

    @ColumnInfo(name = "id_schedules")
    public int id_schedules;

    @ColumnInfo(name = "ab_time")
    public String time;

    @ColumnInfo(name = "ab_out_time")
    public String out_time;

    @ColumnInfo(name = "ab_date")
    public String date;


    @ColumnInfo(name = "lat")
    public Double lat;

    @ColumnInfo(name = "lon")
    public Double lon;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "check")
    public int check;


    @NonNull
    public int getId() {
        return id;
    }

    public int getId_schedules() {
        return id_schedules;
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

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
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

    public void setId_schedules(int id_schedules) {
        this.id_schedules = id_schedules;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
