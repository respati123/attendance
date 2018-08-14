package com.example.acer.attandance_free_feature.db.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.acer.attandance_free_feature.db.WordRoomDatabase;
import com.example.acer.attandance_free_feature.db.dao.AbsensiOfficeDao;
import com.example.acer.attandance_free_feature.db.entities.AbsensiOffice;

import java.util.List;

public class AbsensiOfficeRepository {

    private AbsensiOfficeDao absensiOfficeDao;

    private LiveData<List<AbsensiOffice>> mGetAllAbsensiOffice;

    public AbsensiOfficeRepository(Application app){
        WordRoomDatabase db = WordRoomDatabase.getDatabase(app);
        absensiOfficeDao = db.AbsensiOfficeDao();
        mGetAllAbsensiOffice = absensiOfficeDao.getAllData();
    }


    public LiveData<List<AbsensiOffice>> getmGetAllAbsensiOffice() {
        return mGetAllAbsensiOffice;
    }

    public void insert(AbsensiOffice absensiOffice){
        new InsertAbsensiOfficeAsynTask(absensiOfficeDao).execute(absensiOffice);
    }

    private class InsertAbsensiOfficeAsynTask extends AsyncTask<AbsensiOffice, Void, Void>{

        private AbsensiOfficeDao mAbsensiOfficeDao;

        public InsertAbsensiOfficeAsynTask(AbsensiOfficeDao absensiOfficeDao) {
            mAbsensiOfficeDao = absensiOfficeDao;
        }



        @Override
        protected Void doInBackground(AbsensiOffice... absensiOffices) {
            mAbsensiOfficeDao.Insert(absensiOffices[0]);
            return null;
        }
    }
}
