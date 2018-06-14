package com.hackathon2018.udaan.utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Vinay Bhutani on 4/18/2016.
 */
public class WebService_API {

    HttpURLConnection urlConnection;
    HttpsURLConnection urlConnections;
    int responseCode;
    StringBuffer sb;

    public  String makeServiceCall(String url , String method , String Token , String params,String content_type,String api){

        try {
            if(api.equalsIgnoreCase("Udaan")){
                urlConnections = (HttpsURLConnection) new URL(url).openConnection();

                urlConnections.setRequestMethod(method);
//                String content = "{\"Content-Type\":\"application/x-www-form-urlencoded\",\"Authorization\":\"Basic ZWMxYzg1MGI5YTFjNGY2ZDBkZjJjNmIwMzZhODFhOjJjYWVhNGY1YmJjNTk4NWM4ODQ0N2JhMGNhOGFkNQ==\"}";

                if(method.equals("POST")){
                    urlConnections.setRequestProperty("Content-Type", "application/json");
                    urlConnections.addRequestProperty("Authorization", Token);
                    urlConnections.setDoOutput(true);
                    DataOutputStream outputStream = new DataOutputStream(urlConnections.getOutputStream());
                    outputStream.write(params.getBytes("UTF-8"));
//                    outputStream.writeBytes(Token);
                    outputStream.flush();
                    outputStream.close();
                }
                responseCode = urlConnections.getResponseCode();
                sb = new StringBuffer();
                System.out.println("CODE:"+responseCode);
                if(responseCode == 200){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnections.getInputStream()));
                    String inputLine;
                    while ((inputLine = bufferedReader.readLine()) != null) {
                        sb.append(inputLine);
                    }
                    bufferedReader.close();
                    System.out.println("Anubhav response "+sb.toString());
                }else{
                    System.out.println("Anubhav not 200 "+sb);
                    return null;
                }
                return sb.toString();
            }
            return sb.toString();
        } catch (SocketTimeoutException e) {
            System.out.println("Anubhav exception  "+e.toString());
            String message = "No Responce , Please Try Again...";
            return message.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public int getResponseCode(){
        return responseCode;
    }
}
