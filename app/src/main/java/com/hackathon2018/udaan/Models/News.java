package com.hackathon2018.udaan.Models;

/**
 * Created by aniketvishal on 30/03/18.
 */

public class News {

    String title,news,image;

    public News() {
    }

    public News(String title, String news, String image) {
        this.title = title;
        this.news = news;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
