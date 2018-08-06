package com.example.acer.attandance_free_feature.data;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;

import com.example.acer.attandance_free_feature.db.models.WordViewModel;

public class Model {

    private static final Model ourInstance = new Model();

    public static Model getInstance(){
        return ourInstance;
    }

    public Model(){

    }

    public String username = "",image = "", name = "", nickname = "";
    public int id = 0;

}
