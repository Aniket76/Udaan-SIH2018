package com.hackathon2018.udaan.Models;

import com.hackathon2018.udaan.OthersUserId;

/**
 * Created by aniketvishal on 22/03/18.
 */

public class SearchUsers extends OthersUserId{

    String displayName, status, image;

    public SearchUsers() {
    }

    public SearchUsers(String displayName, String status, String image) {
        this.displayName = displayName;
        this.status = status;
        this.image = image;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
