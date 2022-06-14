package com.example.betaversion1;

import java.util.ArrayList;

public class Books {

    public String name;
    public String author;
    public int genre;
    public ArrayList<String> image;
    public int pages;
    public String id;
    public int ageGroup;
    public String info;

    public Books(String name, String author, int genre, ArrayList<String> image, int pages, String id, int ageGroup, String info){
        this.name=name;
        this.author=author;
        this.genre=genre;
        this.image=image;
        this.pages=pages;
        this.id=id;
        this.ageGroup=ageGroup;
        this.info=info;
    }

    public Books(){

    }

    public String getInfo() {
        return info;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getAgeGroup() {
        return ageGroup;
    }

    public int getGenre() {
        return genre;
    }

    public int getPages() {
        return pages;
    }

    public String getAuthor() {
        return author;
    }

    public String getId() {
        return id;
    }
}
