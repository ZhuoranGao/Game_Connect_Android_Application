package com.example.cs4520_final_project.Models;

public class Game {
    public String name;
    public String developer,publisher,image_URL;
    public int app_id,player_number;


    public Game(String name) {
        this.name = name;
    }

    public Game(){}

    public Game(String name, String developer, String publisher, String image_URL, int app_id, int player_number) {
        this.name = name;
        this.developer = developer;
        this.publisher = publisher;
        this.image_URL = image_URL;
        this.app_id = app_id;
        this.player_number = player_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImage_URL() {
        return image_URL;
    }

    public void setImage_URL(String image_URL) {
        this.image_URL = image_URL;
    }

    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int app_id) {
        this.app_id = app_id;
    }

    public int getPlayer_number() {
        return player_number;
    }

    public void setPlayer_number(int player_number) {
        this.player_number = player_number;
    }
}
