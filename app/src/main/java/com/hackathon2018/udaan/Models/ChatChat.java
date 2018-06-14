package com.hackathon2018.udaan.Models;

import com.hackathon2018.udaan.ChatOtherUserId;

/**
 * Created by aniketvishal on 29/03/18.
 */

public class ChatChat extends ChatOtherUserId {

    private String date;
    String imageurl;
    String name;

    public ChatChat() {
    }

    public ChatChat(String date, String imageurl, String name) {
        this.date = date;
        this.imageurl = imageurl;
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
