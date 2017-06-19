package com.dcits.test.script;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.dcits.bean.user.User;
import com.dcits.bean.web.WebScriptReport;
import com.dcits.util.ExecCmd;

public class RunRubyScript {
	/**
	 * 运行脚本-多个脚本
	 * @param fileNames  场景脚本名集合  包括.rb和.featrue 目录 files/rubyScript
	 * @param publicFile 公共脚本集合 包括.rb  目录 files/publicScript
	 * @param workHome   ruby自动化测试项目工作目录
	 * @param filePath 脚本文件的存放目录
	 * @return
	 */
	@SuppressWarnings("unused")
	public static WebScriptReport runScripts(String[] fileNames,List<String> publicFile, String workHome,String filePath,Integer userId){
		WebScriptReport report = new WebScriptReport();
		String fileMsg = "";
		String featureHome = workHome+"/features";
		String  rbHome= featureHome+"/step_definitions";
		//清空目录内容
		delFilesByPath(featureHome,".feature");
		delFilesByPath(rbHome,".rb");
		
		//检查脚本文件是否都存在，存在就移动到执行目录
		try {
			//操作公共脚本
			for(String s:publicFile){
				File file = new File(filePath+"/publicScript/"+s+".rb");
				if(file.exists()){
					//移动公共脚本
					FileUtils.copyFile(file, new File(rbHome+"/"+s+".rb"));
				}else{
					fileMsg+="公共脚本"+s+".rb"+"文件不存在;\n";
				}
			}		
			//操作场景脚本
			for(String s:fileNames){
				File fileRb = new File(filePath+"/rubyScript/"+s+".rb");
				File fileFeature = new File(filePath+"/rubyScript/"+s+".feature");
				if(fileRb.exists()&&fileFeature.exists()){
					//如果.rb和.feature都存在,移动文件到工作目录
					FileUtils.copyFile(fileRb, new File(rbHome+"/"+s+".rb"));
					FileUtils.copyFile(fileFeature,new File(featureHome+"/"+s+".feature"));													
				}else{
					fileMsg+="场景脚本"+s+".rb或者"+s+".feature文件不存在,跳过执行该脚本;\n";
				}
				
				//通过cmd执行脚本并获取报告路径
				String execS = ExecCmd.exec("cucumber "+featureHome+" -f html -o "+filePath+"/report.html");
				//可能需要返回进行分析，暂时不写
				String timestampS = String.valueOf(System.currentTimeMillis());
				File reHmtl = new File(filePath+"/report.html");
				if(reHmtl.exists()){
					FileUtils.moveFile(reHmtl,new File(filePath+"/rubyReport/report_"+timestampS+".html"));
					report.setReportPath("report_"+timestampS+".html");				
				}else{
					report.setReportPath("");
				}
				report.setCaseNum(fileNames.length);
				report.setTestMark(fileMsg);
				report.setReportName("自动化测试_"+timestampS);								
				report.setTestTime(new Timestamp(System.currentTimeMillis()));
				User user = new User();
				user.setUserId(userId);
				report.setUser(user);				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return report;
	}
	
	/**
	 * 删除指定文件夹下匹配的文件
	 * @param path  要删除的文件夹路径
	 * @param regStr 文件名後綴匹配
	 * @return
	 */
	public static Boolean delFilesByPath(String path,String regStr){
		boolean b=false;
	    File file = new File(path); 
	    File[] tempFile = file.listFiles(); 
	    for(int i = 0; i < tempFile.length; i++){ 
	    if(tempFile[i].getName().endsWith(regStr)){ 
	    	boolean del=deleteFile(path+"/"+tempFile[i].getName());
	    	if(del){
	    		b=true;
	    	}
	    }
	  }
	  return b;
	}
	
	/**
	 * 删除指定的文件
	 * @param path
	 * @return
	 */
	public static Boolean deleteFile(String path){
		 boolean del=false;
		  File file=new File(path);
		  if(file.isFile()){
		   file.delete();
		   del=true;
		  }
		  return del;
	}
}
