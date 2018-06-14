package com.hackathon2018.udaan.Models;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by aniketvishal on 23/03/18.
 */

public class Events {

    String title,desc,time,date,venue,city,postedBy,postingTime,name,image;

    public Events() {
    }

    public Events(String title, String desc, String time, String date, String venue, String city, String postedBy, String postingTime, String name, String image) {
        this.title = title;
        this.desc = desc;
        this.time = time;
        this.date = date;
        this.venue = venue;
        this.city = city;
        this.postedBy = postedBy;
        this.postingTime = postingTime;
        this.name = name;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPostingTime() {
        return postingTime;
    }

    public void setPostingTime(String postingTime) {
        this.postingTime = postingTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
