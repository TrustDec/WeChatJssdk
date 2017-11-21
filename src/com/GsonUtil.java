package com;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GsonUtil {
	public static final Gson gson = new Gson();
	
	@SuppressWarnings("serial")
	public static Map<String, String> fromJson(String json){
		return gson.fromJson(json, 
				new TypeToken<Map<String, String>>() {}.getType());
	}
}
