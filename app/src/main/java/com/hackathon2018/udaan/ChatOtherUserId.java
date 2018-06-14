package com.hackathon2018.udaan;

import io.reactivex.annotations.NonNull;

/**
 * Created by aniketvishal on 28/03/18.
 */

public class ChatOtherUserId {

    public String chatOtherUserId;

    public <T extends ChatOtherUserId> T withOtherUserId(@NonNull final String id){
        this.chatOtherUserId = id;
        return (T) this;
    }
}
