package com.hackathon2018.udaan.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import org.apache.http.NameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Administrator on 5/17/2017.
 */

public class Common {

    public static String BASE_URL="http://www.cashcart.co.in/Registration.svc/";
    public static String ALREADY_REGISTER="alreadyregistered";

    public static String SIGN_UP="signUp";
    public static String LOGIN="login";
    public static String BANNERS="banners";
    public static String BANNERBRANDED="BannerBranded";
    public static String BANNERFOOD="BannerFood";
    public static String BANNERRECHARGE="BannerRecharge";
    public static String BANNERSERVER="BannerServer";
    public static String BANNERSHOPPING="BannerShopping";
    public static String BANNERTRAVEL="BannerTravels";
    public static String PAYTMREQUEST="paytmRequest";
    public static String RECHARGEREQUEST="rechargeRequest";
    public static String OFFERS="offers";
    public static String FINAL_SHOPPING="HasOffersShopping";
    public static String FINAL_BESTOFFERS="BestOffers";
    public static String NEWOFFERS="newoffers";
    public static String FINAL_BRANDED="HasOffersBranded";
    public static String FINAL_FOOD="HasOffersFood";
    public static String FINAL_TRAVEL="HasOffersTravel";
    public static String FINAL_OTHERS="HasOffersOther";
    public static String SEND_EMAIL="SendEmail";

    public static String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public static String getImeiNum(Context context) {
        String identifier = null;
        TelephonyManager tm = null;
        if(context!=null){
            tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        }
        if (tm != null)
            identifier = tm.getDeviceId();
        if (identifier == null || identifier.length() == 0)
            identifier = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return identifier;
    }

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
