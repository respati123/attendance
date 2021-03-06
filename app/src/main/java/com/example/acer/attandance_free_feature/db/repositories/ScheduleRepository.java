package com.example.acer.attandance_free_feature.db.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.acer.attandance_free_feature.db.WordRoomDatabase;
import com.example.acer.attandance_free_feature.db.dao.ScheduleDao;
import com.example.acer.attandance_free_feature.db.entities.Schedules;

import java.util.List;

public class ScheduleRepository {

    private ScheduleDao scheduleDao;

    private LiveData<List<Schedules>> mGetAllSchedule;
    private LiveData<List<Schedules>> mGetDataToday;

    public ScheduleRepository(Application app){

        WordRoomDatabase db = WordRoomDatabase.getDatabase(app);
        scheduleDao = db.ScheduleDao();
        mGetAllSchedule = scheduleDao.getAllSchedule();
        mGetDataToday = scheduleDao.getDataToday();

    }

    public LiveData<List<Schedules>> getmGetDataToday() {
        return mGetDataToday;
    }

    public LiveData<List<Schedules>> getmGetAllSchedule() {
        return mGetAllSchedule;
    }

    public void Insert(Schedules schedules){
        new InsertAsyncTaskSchedule(scheduleDao).execute(schedules);
    }

    public void delete(Schedules schedules){
        new deleteAsyncTaskSchedule(scheduleDao).execute(schedules);
    }
    public void update(Schedules schedule){
        new UpdateAsyncTaskSchedule(scheduleDao).execute(schedule);
    }

    private class InsertAsyncTaskSchedule extends AsyncTask<Schedules, Void, Void>{

        private ScheduleDao mDao;

        public InsertAsyncTaskSchedule(ScheduleDao scheduleDao) {
            mDao = scheduleDao;
        }

        @Override
        protected Void doInBackground(Schedules... schedules) {
            mDao.insert(schedules[0]);
            return null;
        }
    }

    private class UpdateAsyncTaskSchedule extends AsyncTask<Schedules, Void, Void>{
        private ScheduleDao mDao;

        public UpdateAsyncTaskSchedule(ScheduleDao scheduleDao) {
            mDao = scheduleDao;
        }

        @Override
        protected Void doInBackground(Schedules... schedules) {
            mDao.update(schedules[0]);
            return null;
        }
    }

    private class deleteAsyncTaskSchedule extends AsyncTask<Schedules, Void, Void>{

        private ScheduleDao mscheduleDao;

        public deleteAsyncTaskSchedule(ScheduleDao scheduleDao) {
            mscheduleDao = scheduleDao;
        }

        @Override
        protected Void doInBackground(Schedules... schedules) {
            mscheduleDao.delete(schedules[0]);
            return null;
        }
    }
}
