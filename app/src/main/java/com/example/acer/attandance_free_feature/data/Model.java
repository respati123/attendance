package com.example.acer.attandance_free_feature.data;

public class Model {

    private static final Model ourInstance = new Model();

    public static Model getInstance(){
        return ourInstance;
    }

    private Model(){

    }

    public String username = "",image = "", name = "", nickname = "";
    public int id = 0;
}
