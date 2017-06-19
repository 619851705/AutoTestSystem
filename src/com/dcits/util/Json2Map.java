package com.dcits.util;

import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

@SuppressWarnings("unchecked")
public class Json2Map {
	/**
     * json字符串转map集合
     * @param jsonStr json字符串
     * @param map 接收的map
     * @return
     */
    public static Map<String, Object> json2Map(String jsonStr,Map<String, Object> map){
        JSONObject jsonObject = JSONObject.fromObject(jsonStr);    
        map = JSONObject.fromObject(jsonObject);
        //递归map的value,如果
        for(Entry<String, Object> entry:map.entrySet()){
            json2map1(entry, map);
        }
        return map;
    }
    /**
     * json转map,递归调用的方法
     * @param entry 
     * @param map
     * @return
     */
    public static Map<String, Object> json2map1(Entry<String, Object> entry,Map<String, Object> map){
        if(entry.getValue() instanceof Map){
             JSONObject jsonObject= JSONObject.fromObject(entry.getValue());    
             Map<String, Object> map1 = JSONObject.fromObject(jsonObject);
               
             for(Entry<String, Object> entry1:map1.entrySet()){
                 map1=json2map1(entry1, map1);
                 map.put(entry.getKey(), map1);
             }
        }
         return map;
    }
}
