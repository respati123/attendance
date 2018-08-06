package com.example.acer.attandance_free_feature.db.entities;


import android.arch.persistence.room.Embedded;

public class AbsensiAndSchedule {

    @Embedded
    Absensi absensi;

    @Embedded
    Schedules schedules;

    public AbsensiAndSchedule(){}

    public Schedules getSchedules() {
        return schedules;
    }

    public void setSchedules(Schedules schedules) {
        this.schedules = schedules;
    }

    public Absensi getAbsensi() {
        return absensi;
    }

    public void setAbsensi(Absensi absensi) {
        this.absensi = absensi;
    }
}
