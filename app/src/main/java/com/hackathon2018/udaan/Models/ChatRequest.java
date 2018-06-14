package com.hackathon2018.udaan.Models;

import com.hackathon2018.udaan.ChatOtherUserId;

/**
 * Created by aniketvishal on 27/03/18.
 */

public class ChatRequest extends ChatOtherUserId {

    String requestType;
    String imageurl;
    String name;

    public ChatRequest() {
    }

    public ChatRequest(String requestType, String imageurl, String name) {
        this.requestType = requestType;
        this.imageurl = imageurl;
        this.name = name;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
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
