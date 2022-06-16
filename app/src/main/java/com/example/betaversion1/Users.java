package com.example.betaversion1;

public class Users {
    public String name;
    public String image;
    public String email;
    public String uid;
    public String bio;



    public Users(String name, String image, String email, String uid,String bio){
        this.name=name;
        this.image=image;
        this.email=email;
        this.uid=uid;
        this.bio=bio;
    }

    public Users(){

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
