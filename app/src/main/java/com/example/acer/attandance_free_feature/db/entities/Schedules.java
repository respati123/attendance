package com.example.acer.attandance_free_feature.db.entities;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "schedule", foreignKeys = {@ForeignKey(
        entity = Users.class,
        parentColumns = {"id_users"},
        childColumns = {"id_user"},
        onDelete = CASCADE
)})
public class Schedules {


    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id_schedule")
    public int id;

    @ColumnInfo(name = "id_user")
    public int id_users;

    @ColumnInfo(name = "client_name")
    public String client_name;

    @ColumnInfo(name = "job")
    public String job;

    @ColumnInfo(name = "service")
    public String service;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "time")
    public String time;

    @ColumnInfo(name = "meet")
    public String meet;

    @ColumnInfo(name = "description")
    public String desc;

    public String getTime() {
        return time;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public String getName() {
        return client_name;
    }

    public String getDate() {
        return date;
    }

    public String getDesc() {
        return desc;
    }

    public int getId_users() {
        return id_users;
    }

    public String getJob() {
        return job;
    }

    public String getMeet() {
        return meet;
    }

    public String getService() {
        return service;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.client_name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setMeet(String meet) {
        this.meet = meet;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setId_users(int id_users) {
        this.id_users = id_users;
    }
}
