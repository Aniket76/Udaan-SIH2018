package com.hackathon2018.udaan.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {

	private static GsonBuilder gsonBuilder = new GsonBuilder();
	private static Gson gson;
	
	public static Gson getGson(){
//		gsonBuilder.registerTypeAdapter(Timestamp.class, new TimestampDeserializer());
//		gsonBuilder.registerTypeAdapter(Timestamp.class, new TimestampSerializer());
		if(gson == null)
			gson = gsonBuilder.create();
		
		return gson;
	}
}
