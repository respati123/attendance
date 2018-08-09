package com.example.acer.attandance_free_feature.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.acer.attandance_free_feature.db.dao.AbsensiDao;
import com.example.acer.attandance_free_feature.db.dao.ScheduleDao;
import com.example.acer.attandance_free_feature.db.dao.UsersDao;
import com.example.acer.attandance_free_feature.db.dao.WordsDao;
import com.example.acer.attandance_free_feature.db.entities.Absensi;
import com.example.acer.attandance_free_feature.db.entities.AbsensiAndSchedule;
import com.example.acer.attandance_free_feature.db.entities.Schedules;
import com.example.acer.attandance_free_feature.db.entities.Users;
import com.example.acer.attandance_free_feature.db.entities.Words;


@Database(entities = {Words.class, Users.class, Absensi.class, Schedules.class}, version = 5)
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
        private final ScheduleDao mSchedule;

        PopulateDbAsync(WordRoomDatabase db) {
            mDao = db.wordsDao();
            mAbsensi = db.AbsensiDao();
            mUsers = db.usersDao();
            mSchedule = db.ScheduleDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            Words word = new Words("Hello");
            mDao.insert(word);
            word = new Words("World");
            mDao.insert(word);

//            int idUser = mUsers.getUsers().getId();
//
//            Schedules schedules = new Schedules();
//            for(int i = 0; i <= 3; i++){
//                schedules.id_users = idUser;
//                schedules.date = "2009-0" + i + "-15";
//                schedules.time = "13:45:3"+i;
//                schedules.desc = "meeting baru"+i;
//                schedules.meet = "Imah"+i;
//                schedules.client_name = "clozzet"+i;
//                mSchedule.insert(schedules);
//
//            }
//
//            for(int j = 1; j <= 5; j++){
//                Absensi absensi = new Absensi();
//                absensi.id_user = idUser;
//                absensi.id_schedules = 1;
//                absensi.lat = -6.181935;
//                absensi.lon = 106.8031835;
//                absensi.time = "0"+j+":05:46";
//                absensi.date = "2018-08-0"+j;
//                absensi.image = "respati"+j;
//                absensi.type = "checkin";
//                absensi.check = 0;
//                mAbsensi.Insert(absensi);
//            }
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
                        .fallbackToDestructiveMigration()
                        .addCallback(sRoomDatabaseCallback)
                        .build();

            }
        }
        return INSTANCE;
    }

    public abstract WordsDao wordsDao();
    public abstract UsersDao usersDao();
    public abstract AbsensiDao AbsensiDao();
    public abstract ScheduleDao ScheduleDao();


}
