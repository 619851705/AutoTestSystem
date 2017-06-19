/**取出字符串中第一个符合左右边界的字符串
 * *gpf 2016-1-25
 */
package com.dcits.test.message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class CorreLation {
	public static String getstring(String str,String leftbound,String rightbound){
		
	    if (str == null || leftbound == null || rightbound == null){
	           return null; 
	       }
	
		String resCor="无匹配项";
		StringBuffer bf = new StringBuffer("");
		int index_L =  str.indexOf(leftbound); 
		if(index_L!=-1)
		{
			bf.append(str.substring(index_L+leftbound.length()));  //从左边界的尾一直到str结尾，追加给bf 
			str = bf.toString(); //将bf转换成String后，重新赋给str
			int index_R = str.indexOf(rightbound); 
			if(index_R!=-1)
			{
				String getstr = str.substring(0,index_R);
				return getstr;
			}
			return resCor;
		}else
		{
			return resCor;
		}
 

	}
	
	public static String GetReturnCode(String str){
		
		String restr = null;
		String regex = null;
		boolean a = str.contains("<ROOT>"); //判断是XML报文还是Json报文
		if (a){
			
		     regex = "<RETURN_CODE type=\".*\">[^0-9]*([0-9]*)[^0-9]*</RETURN_CODE>"; //定义XMl报文捕获特征
			
		  }
		else{
			regex = "\"RETURN_CODE\":[^0-9]*([0-9]*)[^0-9]*,";//定义json报文捕获特征
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);//匹配类
		while (matcher.find()) {
			//System.out.println(matcher.group(1));//打印中间字符
			restr = matcher.group(1);
		   }
		//System.out.println("Return_code:" + restr); 
		return restr;


	}

}
