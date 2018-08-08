package com.example.acer.attandance_free_feature.db.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;


import com.example.acer.attandance_free_feature.db.dao.UsersDao;
import com.example.acer.attandance_free_feature.db.entities.Absensi;
import com.example.acer.attandance_free_feature.db.entities.AbsensiAndSchedule;
import com.example.acer.attandance_free_feature.db.entities.Schedules;
import com.example.acer.attandance_free_feature.db.entities.Users;
import com.example.acer.attandance_free_feature.db.repositories.AbsensiRepository;
import com.example.acer.attandance_free_feature.db.repositories.ScheduleRepository;
import com.example.acer.attandance_free_feature.db.repositories.UserRepository;
import com.example.acer.attandance_free_feature.db.repositories.WordRepository;
import com.example.acer.attandance_free_feature.db.entities.Words;

import java.util.List;

public class WordViewModel extends AndroidViewModel {
    private UserRepository ur;
    private AbsensiRepository ar;
    private ScheduleRepository sr;

    private LiveData<List<Users>> userList;
    private LiveData<List<Absensi>> absensiList;
    private LiveData<List<Schedules>> scheduleList;
    private LiveData<List<AbsensiAndSchedule>> absensiAndSchedulesList;
    private LiveData<List<Schedules>> mGetDataScheduleToday ;

    public WordViewModel(Application app){
        super(app);
        ur = new UserRepository(app);
        userList = ur.getmGetAllUser();

        ar = new AbsensiRepository(app);
        absensiList = ar.getmGetAllAbsensi();

        sr = new ScheduleRepository(app);
        scheduleList = sr.getmGetAllSchedule();
        mGetDataScheduleToday = sr.getmGetDataToday();


    }
    //schedule
    public LiveData<List<Schedules>> getAllSchedule() {
        return scheduleList;
    }

    public void insert(Schedules schedules){
        sr.Insert(schedules);
    }

    public void update(Schedules schedule){sr.update(schedule);}

    public void delete(Schedules schedules) {
        sr.delete(schedules);
    }

    public LiveData<List<Schedules>> getmGetDataScheduleToday() {
        return mGetDataScheduleToday;
    }

    //absensi
    public LiveData<List<Absensi>> getAbsensiList() {
        return absensiList;
    }

    public void insert(Absensi absensi){
        ar.insert(absensi);
    }

    public LiveData<List<AbsensiAndSchedule>> getAbsensiAndSchedulesList(String from, String to) {
         return ar.getmGetAllAbsensiWithSchedule(from, to);
    }

    //users
    public LiveData<List<Users>> getUserList() {
        return userList;
    }

    public void insert(Users users){
        ur.insert(users);
    }

    public void updateData(Users users){
        ur.update(users);
    }

    public void getUsers(){
        ur.getUsers();
    }
}
