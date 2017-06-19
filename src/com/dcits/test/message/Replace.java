/**替换字符中所有符合条件的字符串
 * *gpf 2016-1-25
 */
package com.dcits.test.message;

public class Replace {
	public static String replacestring(String from, String to, String source)   
    {     
       if (source == null || from == null || to == null)     
           return null;     
       StringBuffer bf = new StringBuffer("");     
       int index = -1;     
       while ((index = source.indexOf(from)) != -1)   
       {     

           bf.append(source.substring(0, index) + to);
           source = source.substring(index + from.length());     
           index = source.indexOf(from);     
       }     
       bf.append(source);     
       return bf.toString();     
   }  
}