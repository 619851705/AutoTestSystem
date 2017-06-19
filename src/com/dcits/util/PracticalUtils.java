package com.dcits.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dcits.bean.message.TestReport;
import com.dcits.bean.message.TestResult;

/**
 * ʵ��С������
 * @author Administrator
 *
 */
public class PracticalUtils {
	
	/**
	 * �ж��ַ����Ƿ�Ϊ����
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){ 
		   Pattern pattern = Pattern.compile("[0-9]*"); 
		   Matcher isNum = pattern.matcher(str);
		   if( !isNum.matches() ){
		       return false; 
		   } 
		   return true; 
		}
	
	/**
	 * ���ɾ�̬htmlҳ�湩����ʹ��,�����ļ�·��
	 * @param report
	 * @param results
	 * @return
	 */
	public static String CreateReportHtml(TestReport report,Set<TestResult> results){
		
		
		return null;
	}
	
	/**
	 * arrayList��toString����
	 * @param list
	 * @return
	 */
	public static String arrayListToString(ArrayList<String> list){
		String returnStr = "";
		for(String s:list){
			returnStr+="["+s+"]";
		}
		return returnStr;
	}
	
	/**
	  * 
	  * @param s  Ҫ�������ַ���
	  * @param string  Ҫɾ�����ַ�
	  * @param i  ɾ���ڼ���
	  * @return
	  */
	 public static String removeChar(String s,String string,int i){
	  if(i==1){
	   int j=s.indexOf(string);
	   s=s.substring(0, j)+s.substring(j+1);
	   i--;
	   return s;
	  }else{
	   int j=s.indexOf(string);
	   i--;
	   return s.substring(0, j+1)+removeChar(s.substring(j+1), string, i);
	  }

	}
	 
	 
	 /**
	  * �滻sql����Ҫ�滻�Ĳ���
	  * ������ʽ<������>
	  * Web�Զ���������ʹ�õ�
	  * @param sqlStr
	  * @param map
	  * @return
	  */
	 public static String replaceSqlStr(String sqlStr,Map<String,Object> map){
		 String regex = "(<[a-zA-Z0-9]*>)";
		 Pattern pattern = Pattern.compile(regex);
		 List<String> regStrs = new ArrayList<String>();
		 Matcher matcher = pattern.matcher(sqlStr);//ƥ����
		 while (matcher.find()) {
			regStrs.add(matcher.group());
		 }
		 for(String s:regStrs){
			 if(map.get(s)!=null){
				 sqlStr = sqlStr.replaceAll(s, (String)map.get(s));
			 }
		 }
		 return sqlStr;
	 }
	 
	 /**
	  * �滻sql����еĲ���
	  * ������ʽ<�ڵ����ƻ���·��>
	  * �ӿ��Զ�����ʹ�õ�
	  * @param sqlStr
	  * @param requestMap
	  * @param requestJson
	  * @return
	  */
	 public static String replaceSqlStr(String sqlStr,Map<String,String> requestMap,String requestJson){
		 String regex = "(<[a-zA-Z0-9_.]*>)";
		 Pattern pattern = Pattern.compile(regex);
		 List<String> regStrs = new ArrayList<String>();
		 Matcher matcher = pattern.matcher(sqlStr);//ƥ����
		 while (matcher.find()) {
			regStrs.add(matcher.group());
		 }
		 for(String s:regStrs){
			 String regS = s.substring(1, s.length()-1);
			 if(s.indexOf(".")!=-1){
				 regS = JsonUtil.getObjectByJson(requestJson, regS, JsonUtil.TypeEnum.string);
				 if(regS!=null){
					 sqlStr = sqlStr.replaceAll(s, regS);
				 }				
			 }else{
				 if(requestMap.get(regS)!=null){
					 sqlStr = sqlStr.replaceAll(s, requestMap.get(regS));
				 }
			 }			 
		 }
		 return sqlStr;
	 }
}
