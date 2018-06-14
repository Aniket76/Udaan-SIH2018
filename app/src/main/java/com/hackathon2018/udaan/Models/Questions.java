package com.hackathon2018.udaan.Models;

import com.hackathon2018.udaan.QuestionId;

/**
 * Created by aniketvishal on 24/03/18.
 */

public class Questions extends QuestionId{

    String question,postedBy,postingTime,name,image;

    public Questions() {
    }

    public Questions(String question, String postedBy, String postingTime, String name, String image) {
        this.question = question;
        this.postedBy = postedBy;
        this.postingTime = postingTime;
        this.name = name;
        this.image = image;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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
