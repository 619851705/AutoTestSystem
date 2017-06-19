package com.dcits.task;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientRequest {
	 public static String postForm(String requestUrl,Integer id) {  
	        // ����Ĭ�ϵ�httpClientʵ��.    
	        CloseableHttpClient httpclient = HttpClients.createDefault(); 
	        // ����httppost    
	        HttpPost httppost = new HttpPost(requestUrl);  
	        
	        String returnJson = "";
	        // ������������    
	        List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();  
	        formparams.add(new BasicNameValuePair("setId", String.valueOf(id)));  
	        UrlEncodedFormEntity uefEntity;  
	        try {  
	            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
	            httppost.setEntity(uefEntity);   
	            CloseableHttpResponse response = httpclient.execute(httppost);  
	            try {  
	                HttpEntity entity = response.getEntity();  
	                if (entity != null) {  	 
	                	returnJson = EntityUtils.toString(entity, "UTF-8");
	                }  
	            } finally {  
	                response.close();  
	            }  
	        } catch (ClientProtocolException e) {  
	            e.printStackTrace();  
	        } catch (UnsupportedEncodingException e1) {  
	            e1.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        } finally {  
	            // �ر�����,�ͷ���Դ    
	            try {  
	                httpclient.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	        return returnJson;
	    }  
}
