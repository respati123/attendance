package com.example.acer.attandance_free_feature.db.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Update;
import android.os.AsyncTask;

import com.example.acer.attandance_free_feature.db.WordRoomDatabase;
import com.example.acer.attandance_free_feature.db.dao.UsersDao;
import com.example.acer.attandance_free_feature.db.entities.Users;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.xml.transform.Result;

public class UserRepository {

    private UsersDao ud;
    private LiveData<List<Users>> mGetAllUser;

    public UserRepository(Application app){
        WordRoomDatabase db = WordRoomDatabase.getDatabase(app);
        ud = db.usersDao();
        mGetAllUser = ud.getAllUsers();

    }

    public LiveData<List<Users>> getmGetAllUser() {
        return mGetAllUser;
    }

    public void insert(Users users){
        new insertUserAsyncTask(ud).execute(users);
    }

    public void update(Users users){
        new updateDataAsyncTask(ud).execute(users);
    }

    public void getUsers() {
        ud.getUsers();
    }

    private static class insertUserAsyncTask extends AsyncTask<Users, Void, Void> {

        private UsersDao mAsyncUserDao;

        insertUserAsyncTask(UsersDao ud) {
            mAsyncUserDao = ud;
        }

        @Override
        protected Void doInBackground(final Users... users) {
            mAsyncUserDao.insert(users[0]);
            return null;
        }
    }

    private class updateDataAsyncTask extends AsyncTask<Users, Void, Void>{

        private UsersDao mAsyncUserDao;
        updateDataAsyncTask(UsersDao ud) {
            mAsyncUserDao = ud;
        }


        @Override
        protected Void doInBackground(Users... users) {
            mAsyncUserDao.updateUsers(users[0]);
            return null;
        }
    }
}
