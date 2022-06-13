package com.example.betaversion1;

import java.util.ArrayList;

public class Sales {

    public String Date;
    public boolean Status;
    public int Price;
    public int Condition;
    public ArrayList<String> Image;
    public String UserId;
    public String BookId;
    public String Info;

    public Sales(String Date, boolean Status, int Price, int Condition, ArrayList<String> Image,
                 String UserId, String BookId, String Info){
        this.Date=Date;
        this.Status=Status;
        this.Price=Price;
        this.Condition=Condition;
        this.Image=Image;
        this.UserId=UserId;
        this.BookId=BookId;
        this.Info=Info;
    }

    public Sales(){

    }

    public String getDate() {
        return Date;
    }

    public ArrayList<String> getImage() {
        return Image;
    }

    public int getCondition() {
        return Condition;
    }

    public int getPrice() {
        return Price;
    }

    public String getBookId() {
        return BookId;
    }

    public String getInfo() {
        return Info;
    }

    public String getUserId() {
        return UserId;
    }
}
