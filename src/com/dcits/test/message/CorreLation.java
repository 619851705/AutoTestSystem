/**ȡ���ַ����е�һ���������ұ߽���ַ���
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
	
		String resCor="��ƥ����";
		StringBuffer bf = new StringBuffer("");
		int index_L =  str.indexOf(leftbound); 
		if(index_L!=-1)
		{
			bf.append(str.substring(index_L+leftbound.length()));  //����߽��βһֱ��str��β��׷�Ӹ�bf 
			str = bf.toString(); //��bfת����String�����¸���str
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
		boolean a = str.contains("<ROOT>"); //�ж���XML���Ļ���Json����
		if (a){
			
		     regex = "<RETURN_CODE type=\".*\">[^0-9]*([0-9]*)[^0-9]*</RETURN_CODE>"; //����XMl���Ĳ�������
			
		  }
		else{
			regex = "\"RETURN_CODE\":[^0-9]*([0-9]*)[^0-9]*,";//����json���Ĳ�������
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);//ƥ����
		while (matcher.find()) {
			//System.out.println(matcher.group(1));//��ӡ�м��ַ�
			restr = matcher.group(1);
		   }
		//System.out.println("Return_code:" + restr); 
		return restr;


	}

}
