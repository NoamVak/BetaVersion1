package com.example.betaversion1;

import java.util.ArrayList;

public class Books {

    public String Name;
    public String Author;
    public int Genre;
    public ArrayList<String> Image;
    public int Pages;
    public String Id;
    public int AgeGroup;
    public String Info;

    public Books(String Name, String Author, int Genre, ArrayList<String> Image, int Pages, String Id, int AgeGroup, String Info){
        this.Name=Name;
        this.Author=Author;
        this.Genre=Genre;
        this.Image=Image;
        this.Pages=Pages;
        this.Id=Id;
        this.AgeGroup=AgeGroup;
        this.Info=Info;
    }

    public Books(){

    }

    public String getInfo() {
        return Info;
    }

    public ArrayList<String> getImage() {
        return Image;
    }

    public String getName() {
        return Name;
    }

    public int getAgeGroup() {
        return AgeGroup;
    }

    public int getGenre() {
        return Genre;
    }

    public int getPages() {
        return Pages;
    }

    public String getAuthor() {
        return Author;
    }

    public String getId() {
        return Id;
    }
}
