package com.hackathon2018.udaan;

import io.reactivex.annotations.NonNull;

/**
 * Created by aniketvishal on 24/03/18.
 */

public class QuestionId {

    public String questionId;

    public <T extends QuestionId> T withQuestionId(@NonNull final String id){
        this.questionId = id;
        return (T) this;
    }

}
