package com.example.acer.attandance_free_feature.db.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.acer.attandance_free_feature.db.WordRoomDatabase;
import com.example.acer.attandance_free_feature.db.dao.AbsensiDao;
import com.example.acer.attandance_free_feature.db.entities.Absensi;
import com.example.acer.attandance_free_feature.db.entities.AbsensiAndSchedule;
import com.example.acer.attandance_free_feature.db.entities.Schedules;

import java.util.List;

public class AbsensiRepository  {

    private AbsensiDao absensiDao;

    private LiveData<List<Absensi>> mGetAllAbsensi;

    private LiveData<List<AbsensiAndSchedule>> mGetAllAbsensiWithSchedule;

    public AbsensiRepository (Application application){
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        absensiDao = db.AbsensiDao();
        mGetAllAbsensi = absensiDao.getAllAbsensi();

    }

    public LiveData<List<Absensi>> getmGetAllAbsensi() {
        return mGetAllAbsensi;
    }

    public LiveData<List<AbsensiAndSchedule>> getmGetAllAbsensiWithSchedule(String from, String to) {
        return absensiDao.getAllAbsensiRelationSchedule(from, to);
    }

    public void insert(Absensi absensi){
        new insertAbsensiAsyncTask(absensiDao).execute(absensi);
    }



    private class insertAbsensiAsyncTask extends AsyncTask<Absensi, Void, Void>{
        private AbsensiDao mAbsensiDao;

        insertAbsensiAsyncTask(AbsensiDao absensiDao) {
            mAbsensiDao = absensiDao;
        }

        @Override
        protected Void doInBackground(Absensi... absensis) {
            mAbsensiDao.Insert(absensis[0]);
            return null;
        }
    }
}
