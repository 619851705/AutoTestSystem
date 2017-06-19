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
	 * ���нű�-����ű�
	 * @param fileNames  �����ű�������  ����.rb��.featrue Ŀ¼ files/rubyScript
	 * @param publicFile �����ű����� ����.rb  Ŀ¼ files/publicScript
	 * @param workHome   ruby�Զ���������Ŀ����Ŀ¼
	 * @param filePath �ű��ļ��Ĵ��Ŀ¼
	 * @return
	 */
	@SuppressWarnings("unused")
	public static WebScriptReport runScripts(String[] fileNames,List<String> publicFile, String workHome,String filePath,Integer userId){
		WebScriptReport report = new WebScriptReport();
		String fileMsg = "";
		String featureHome = workHome+"/features";
		String  rbHome= featureHome+"/step_definitions";
		//���Ŀ¼����
		delFilesByPath(featureHome,".feature");
		delFilesByPath(rbHome,".rb");
		
		//���ű��ļ��Ƿ񶼴��ڣ����ھ��ƶ���ִ��Ŀ¼
		try {
			//���������ű�
			for(String s:publicFile){
				File file = new File(filePath+"/publicScript/"+s+".rb");
				if(file.exists()){
					//�ƶ������ű�
					FileUtils.copyFile(file, new File(rbHome+"/"+s+".rb"));
				}else{
					fileMsg+="�����ű�"+s+".rb"+"�ļ�������;\n";
				}
			}		
			//���������ű�
			for(String s:fileNames){
				File fileRb = new File(filePath+"/rubyScript/"+s+".rb");
				File fileFeature = new File(filePath+"/rubyScript/"+s+".feature");
				if(fileRb.exists()&&fileFeature.exists()){
					//���.rb��.feature������,�ƶ��ļ�������Ŀ¼
					FileUtils.copyFile(fileRb, new File(rbHome+"/"+s+".rb"));
					FileUtils.copyFile(fileFeature,new File(featureHome+"/"+s+".feature"));													
				}else{
					fileMsg+="�����ű�"+s+".rb����"+s+".feature�ļ�������,����ִ�иýű�;\n";
				}
				
				//ͨ��cmdִ�нű�����ȡ����·��
				String execS = ExecCmd.exec("cucumber "+featureHome+" -f html -o "+filePath+"/report.html");
				//������Ҫ���ؽ��з�������ʱ��д
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
				report.setReportName("�Զ�������_"+timestampS);								
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
	 * ɾ��ָ���ļ�����ƥ����ļ�
	 * @param path  Ҫɾ�����ļ���·��
	 * @param regStr �ļ�����Yƥ��
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
	 * ɾ��ָ�����ļ�
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
