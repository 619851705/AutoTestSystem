package com.dcits.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class GJsonFormatUtil {
	//json字符串格式化工具
	public static String formatJsonStr(String uglyJSONString){
		 Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        JsonParser jp = new JsonParser();
	        try {
	        	JsonElement je = jp.parse(uglyJSONString);
		        String prettyJsonString = gson.toJson(je);
		        return prettyJsonString;
			} catch (Exception e) {
				return null;
			}
	        
	}
}
