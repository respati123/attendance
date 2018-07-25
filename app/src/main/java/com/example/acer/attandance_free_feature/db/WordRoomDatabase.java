package com.example.acer.attandance_free_feature.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.acer.attandance_free_feature.db.dao.AbsensiDao;
import com.example.acer.attandance_free_feature.db.dao.UsersDao;
import com.example.acer.attandance_free_feature.db.dao.WordsDao;
import com.example.acer.attandance_free_feature.db.entities.Absensi;
import com.example.acer.attandance_free_feature.db.entities.Users;
import com.example.acer.attandance_free_feature.db.entities.Words;


@Database(entities = {Words.class, Users.class, Absensi.class}, version = 1)
public abstract class WordRoomDatabase extends RoomDatabase {

    private static WordRoomDatabase INSTANCE;

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final WordsDao mDao;
        private final AbsensiDao mAbsensi;
        private final UsersDao mUsers;

        PopulateDbAsync(WordRoomDatabase db) {
            mDao = db.wordsDao();
            mAbsensi = db.AbsensiDao();
            mUsers = db.usersDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            Words word = new Words("Hello");
            mDao.insert(word);
            word = new Words("World");
            mDao.insert(word);

            int idUser = mUsers.getUsers().getId();


            Absensi absensi = new Absensi();
            absensi.id_user = idUser;
            absensi.lat = -6.181935;
            absensi.lon = 106.8031835;
            absensi.time = "2009-06-15T13:45:30";
            absensi.image = "respati";
            absensi.type = "checkin";
            mAbsensi.Insert(absensi);

            return null;

        }
    }

    public static WordRoomDatabase getDatabase(final Context ctx){
        if(INSTANCE == null){
            synchronized (WordRoomDatabase.class){
                INSTANCE = Room.databaseBuilder(
                        ctx.getApplicationContext(),
                        WordRoomDatabase.class,
                        "words")
                        .build();

            }
        }
        return INSTANCE;
    }

    public abstract WordsDao wordsDao();
    public abstract UsersDao usersDao();
    public abstract AbsensiDao AbsensiDao();


}
