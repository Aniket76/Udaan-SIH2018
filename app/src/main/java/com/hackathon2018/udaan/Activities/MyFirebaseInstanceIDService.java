package com.hackathon2018.udaan.Activities;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by vinculum on 30/3/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {

        //For registration of token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //To displaying token on logcat
        Log.d("TOKEN: ", refreshedToken);
        System.out.println("ANUBHAV " +refreshedToken.toString());

    }
}
