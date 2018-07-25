package com.example.acer.attandance_free_feature.db.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.acer.attandance_free_feature.db.WordRoomDatabase;
import com.example.acer.attandance_free_feature.db.dao.WordsDao;
import com.example.acer.attandance_free_feature.db.entities.Words;

import java.util.List;


public class WordRepository {
    private WordsDao wDao;
    private LiveData<List<Words>> wordList;

    public WordRepository(Application app){
        WordRoomDatabase db = WordRoomDatabase.getDatabase(app);
        wDao = db.wordsDao();
        wordList = wDao.getAllWords();
    }

    public LiveData<List<Words>> getAllWords(){
        return wordList;
    }

    public void insertWord(Words word){
        new insertAsyncTask(wDao).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<Words, Void, Void> {

        private WordsDao mAsyncTaskDao;

        insertAsyncTask(WordsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Words... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}

