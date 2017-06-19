package com.dcits.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class ExecCmd {
	
	public static String exec(String cmd){
		BufferedReader br = null;
		String returnStr = "";
		try {
			Process p = Runtime.getRuntime().exec("cmd /c "+cmd);
			br = new BufferedReader(new InputStreamReader(p.getInputStream(),Charset.forName("utf-8")));
			String line = null;
			while ((line = br.readLine()) != null) {
				returnStr+=line+"\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("["+cmd+"]Ö´ÐÐ·µ»ØÄÚÈÝ£º\n"+returnStr);
		return returnStr;
	}
}
