package com.example.betaversion1;

public class Users {
    public String name;
    public String image;
    public String address;
    public String phoneNum;
    public String city;
    public String email;
    public String uid;
    public String bio;



    public Users(String name, String image, String address, String phoneNum, String city, String email, String uid,String bio){
        this.name=name;
        this.image=image;
        this.address=address;
        this.phoneNum=phoneNum;
        this.city=city;
        this.email=email;
        this.uid=uid;
        this.bio=bio;
    }

    public Users(){

    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getUid() {
        return uid;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
