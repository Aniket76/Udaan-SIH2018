package com.hackathon2018.udaan.Models;

/**
 * Created by aniketvishal on 30/03/18.
 */

public class Message {

    private String msg,type,name;

    public Message() {
    }

    public Message(String msg, String type, String name) {
        this.msg = msg;
        this.type = type;
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
