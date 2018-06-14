package com.hackathon2018.udaan.Models;

/**
 * Created by aniketvishal on 24/03/18.
 */

public class Answers {

    String answer,postedBy,postingTime,name,image;

    public Answers() {
    }

    public Answers(String answer, String postedBy, String postingTime, String name, String image) {
        this.answer = answer;
        this.postedBy = postedBy;
        this.postingTime = postingTime;
        this.name = name;
        this.image = image;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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
