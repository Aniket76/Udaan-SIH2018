package com.hackathon2018.udaan.asyncTasks;

import android.os.AsyncTask;

import com.hackathon2018.udaan.utility.WebService_API;


//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Created by Administrator on 12/13/2016.
 */

public class RequestAPI extends AsyncTask<String,String,String> {

    WebService_API service_api;
    public AsyncResponse delegate = null;

    @Override
    protected String doInBackground(String... params) {
        String result=null;
            service_api=new WebService_API();

        result=service_api.makeServiceCall(params[0],params[1],params[2],params[3],params[4],params[5]);

        System.out.println("params:"+params[0]+":"+params[1]+":"+params[2]+":"+result);
        return result;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if(response != null){
            delegate.processFinish(response);
        }
//        System.out.println("anubhav responce "+response.toString());

    }
}
