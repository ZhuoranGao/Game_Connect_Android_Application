package com.example.cs4520_final_project.Models;

import java.util.ArrayList;

public class User {
    private String uid,name,user_name,email;
    private String Location;
    private String imageURL;
    private ArrayList<String> games;
    private ArrayList<String> friend;



//    public User(String uid,String name, String display_name, String email,String imageURL) {
//        this.name = name;
//        this.user_name = display_name;
//        this.email = email;
//        this.uid = uid;
//        this.imageURL = imageURL;
//    }

    public User(){

    }

//    public User(String uid, String name, String user_name, String email) {
//        this.uid = uid;
//        this.name = name;
//        this.user_name = user_name;
//        this.email = email;
//    }


    public ArrayList getGames() {
        return games;
    }

    public void setGames(ArrayList<String> games) {
        this.games = games;
    }

    public User(String uid, String name, String user_name, String email, String location, String imageURL, ArrayList<String> games,ArrayList<String> friend) {
        this.uid = uid;
        this.name = name;
        this.user_name = user_name;
        this.email = email;
        Location = location;
        this.imageURL = imageURL;
        this.games=games;
        this.friend=friend;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public ArrayList<String> getFriend() {
        return friend;
    }

    public void setFriend(ArrayList<String> friend) {
        this.friend = friend;
    }
}
