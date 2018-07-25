package com.example.acer.attandance_free_feature.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "words")
public class Words {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String word;

    public Words(@NonNull String word){
        this.word = word;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWord(@NonNull String word) {
        this.word = word;
    }

    public String getWord(){
        return this.word;
    }

    public int getId(){
        return this.id;
    }
}
