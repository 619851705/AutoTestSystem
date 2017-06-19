package com.dcits.test.script;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Testaa {
	public static void main(String[] args) {
		String[] ss = {"1","2","3"};
		List<String> s1 = new ArrayList<String>();
		s1.add("4");
		s1.add("5");
		s1.addAll(Arrays.asList(ss));
		String[] s2 = (String[]) s1.toArray(new String[s1.size()]);
		
		System.out.println(s2.length);
		for(String s:s2){
			System.out.println(s);
		}
	}
}
