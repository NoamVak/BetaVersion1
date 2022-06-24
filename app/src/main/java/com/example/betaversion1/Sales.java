package com.example.betaversion1;

import java.util.ArrayList;

public class Sales {

    public String date;
    public boolean status;
    public int price;
    public int condition;
    public String image;
    public String address;
    public String phoneNum;
    public String city;
    public String userId;
    public String bookId;
    public String info;

    public Sales(String date, boolean status, int price, int condition, String image,String address,String phoneNum,String city,
                 String userId, String bookId, String info){
        this.address=address;
        this.phoneNum=phoneNum;
        this.city=city;
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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getCity() {
        return city;
    }

    public boolean getStatus(){
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
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
