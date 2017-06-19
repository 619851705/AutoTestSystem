package com.dcits.util;

import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

@SuppressWarnings("unchecked")
public class Json2Map {
	/**
     * json�ַ���תmap����
     * @param jsonStr json�ַ���
     * @param map ���յ�map
     * @return
     */
    public static Map<String, Object> json2Map(String jsonStr,Map<String, Object> map){
        JSONObject jsonObject = JSONObject.fromObject(jsonStr);    
        map = JSONObject.fromObject(jsonObject);
        //�ݹ�map��value,���
        for(Entry<String, Object> entry:map.entrySet()){
            json2map1(entry, map);
        }
        return map;
    }
    /**
     * jsonתmap,�ݹ���õķ���
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
