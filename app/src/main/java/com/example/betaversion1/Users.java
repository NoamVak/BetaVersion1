package com.example.betaversion1;

public class Users {
    public String Name;
    public String Image;
    public String Address;
    public String PhoneNum;
    public String City;
    public String Email;
    public int Age;
    public String Uid;


    public Users(String Name, String Image, String Address, String PhoneNum, String City, String Email, int Age, String Uid){
        this.Name=Name;
        this.Image=Image;
        this.Address=Address;
        this.PhoneNum=PhoneNum;
        this.City=City;
        this.Email=Email;
        this.Age=Age;
        this.Uid=Uid;
    }

    public Users(){

    }

    public int getAge() {
        return Age;
    }

    public String getAddress() {
        return Address;
    }

    public String getCity() {
        return City;
    }

    public String getEmail() {
        return Email;
    }

    public String getImage() {
        return Image;
    }

    public String getName() {
        return Name;
    }

    public String getPhoneNum() {
        return PhoneNum;
    }

    public String getUid() {
        return Uid;
    }
}
