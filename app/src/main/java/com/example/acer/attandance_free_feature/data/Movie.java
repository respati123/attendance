package com.example.acer.attandance_free_feature.data;

public class Movie {

    private String title, genre, years;

    public Movie(){

    }

    public Movie(String title, String genre, String year){
        this.title = title;
        this.genre = genre;
        this.years = year;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public String getGenre() {
        return genre;
    }

    public String getTitle() {
        return title;
    }

    public String getYears() {
        return years;
    }
}
