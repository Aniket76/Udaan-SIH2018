package com.hackathon2018.udaan.utility;

/**
 * Created by piyush.singh on 3/11/2016.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {
    public static boolean isConnectingToInternet(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.getState() == NetworkInfo.State.CONNECTED)
                    return true;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
