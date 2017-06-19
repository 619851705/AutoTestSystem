package com.dcits.action.web;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dcits.bean.user.User;
import com.dcits.bean.web.WebTestRmi;
import com.dcits.service.web.WebCaseService;
import com.dcits.service.web.WebCaseSetService;
import com.dcits.service.web.WebTestRmiService;
import com.dcits.util.StrutsMaps;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@Controller
public class WebTestRmiAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	@Autowired
	private WebTestRmiService service;
	@Autowired
	private WebCaseService cService;
	@Autowired
	private WebCaseSetService sService;
	
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	private Integer caseId;
	
	private Integer setId;
	
	private String fileName;
	
	
	//获取个人的当前测试任务
	public String taskList(){
		List<WebTestRmi> wtrs = service.getUserTask(((User)StrutsMaps.getSessionMap().get("user")).getUserId());
		for(WebTestRmi r:wtrs){
			try {
				r.setStatus(r.getStatus());				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		jsonMap.put("data", wtrs);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	
	//发送远程测试请求
	public String runTest(){
		User user=(User) StrutsMaps.getSessionMap().get("user");
		List<WebTestRmi> tasks=service.getByUser(user.getUserId());
		if(tasks.size()>0){
			jsonMap.put("msg", "你当前有尚未测试完成或者正在进行测试的测试任务,请查看当前任务列表");
			return SUCCESS;
		}
		WebTestRmi test=new WebTestRmi(user,"0",caseId,new Timestamp(System.currentTimeMillis()),"1","","");
		test.setTaskName(cService.getCase(caseId).getCaseName());
		if(setId!=null){
			test.setTestMode("1");
			test.setRelatedId(setId);
			test.setTaskName(sService.get(setId).getSetName());
		}
		service.save(test);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//接受从客户端传送过来的测试截图
	public String recFile(){
		try {
		 	HttpServletRequest request = ServletActionContext.getRequest();  
		 	ActionContext ac = ActionContext.getContext();
		 	ServletContext sc = (ServletContext) ac.get(ServletActionContext.SERVLET_CONTEXT);     
	        String rootPath= sc.getRealPath("/");
		 	String dirPath="screenshots/"; 			
			InputStream input = request.getInputStream();  
			String fileFullPath = rootPath + dirPath + fileName;   		
			FileOutputStream fos = new FileOutputStream(fileFullPath);  			
			int size = 0;  
			byte[] buffer = new byte[1024];  
			while ((size = input.read(buffer, 0, 1024)) != -1) {  
			    fos.write(buffer, 0, size);  
			}  
			fos.close();  
			input.close();  
			jsonMap.put("returnCode", 0);
		} catch (Exception e) {
			e.printStackTrace();
			jsonMap.put("returnCode", 1);
		}				
		return SUCCESS;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	
	public void setSetId(Integer setId) {
		this.setId = setId;
	}
	
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}
	
}
