package com.hackathon2018.udaan;

import io.reactivex.annotations.NonNull;

/**
 * Created by aniketvishal on 23/03/18.
 */

public class OthersUserId {

    public String otherUserId;

    public <T extends OthersUserId> T withId(@NonNull final String id){
        this.otherUserId = id;
        return (T) this;
    }

}
