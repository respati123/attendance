package com.example.acer.attandance_free_feature.db.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "absensi_office", foreignKeys =
        {@ForeignKey(entity = Users.class,
                parentColumns = {"id_users"},
                childColumns = {"id_user"},
                onDelete = CASCADE)
        })
public class AbsensiOffice {

    public AbsensiOffice(int id_user, String time,String date,
                         Double lat,Double lon,String image, String type, String check){

        this.id_user = id_user;
        this.time = time;
        this.date = date;
        this.lat = lat;
        this.lon = lon;
        this.image = image;
        this.type = type;
        this.check = check;
    }

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "id_user")
    private int id_user;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "out_time")
    private String out_time;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "lat")
    private Double lat;

    @ColumnInfo(name = "lon")
    private Double lon;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "check")
    private String check;

    @ColumnInfo(name = "checkout_lat")
    private Double checkout_lat;

    @ColumnInfo(name = "checkout_lon")
    private Double checkout_lon;


    @NonNull
    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public int getId_user() {
        return id_user;
    }

    public String getImage() {
        return image;
    }

    public Double getCheckout_lat() {
        return checkout_lat;
    }

    public Double getCheckout_lon() {
        return checkout_lon;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public String getCheck() {
        return check;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCheckout_lat(Double checkout_lat) {
        this.checkout_lat = checkout_lat;
    }

    public void setCheckout_lon(Double checkout_lon) {
        this.checkout_lon = checkout_lon;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }
}
