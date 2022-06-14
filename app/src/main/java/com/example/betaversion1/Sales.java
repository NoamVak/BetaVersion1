package com.example.betaversion1;

import java.util.ArrayList;

public class Sales {

    public String date;
    public boolean status;
    public int price;
    public int condition;
    public ArrayList<String> image;
    public String userId;
    public String bookId;
    public String info;

    public Sales(String date, boolean status, int price, int condition, ArrayList<String> image,
                 String userId, String bookId, String info){
        this.date=date;
        this.status=status;
        this.price=price;
        this.condition=condition;
        this.image=image;
        this.userId=userId;
        this.bookId=bookId;
        this.info=info;
    }

    public Sales(){

    }

    public String getDate() {
        return date;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public int getCondition() {
        return condition;
    }

    public int getPrice() {
        return price;
    }

    public String getBookId() {
        return bookId;
    }

    public String getInfo() {
        return info;
    }

    public String getUserId() {
        return userId;
    }
}
