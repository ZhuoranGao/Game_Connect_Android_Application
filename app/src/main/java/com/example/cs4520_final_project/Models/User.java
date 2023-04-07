package com.example.cs4520_final_project.Models;

public class User {
    public String uid,name,user_name,email;
    public String Location;
    public String imageURL;



    public User(String uid,String name, String display_name, String email,String imageURL) {
        this.name = name;
        this.user_name = display_name;
        this.email = email;
        this.uid = uid;
        this.imageURL = imageURL;
    }

    public User(){

    }

    public User(String uid, String name, String user_name, String email) {
        this.uid = uid;
        this.name = name;
        this.user_name = user_name;
        this.email = email;
    }


    public User(String uid, String name, String user_name, String email, String location, String imageURL) {
        this.uid = uid;
        this.name = name;
        this.user_name = user_name;
        this.email = email;
        Location = location;
        this.imageURL = imageURL;
    }
}
