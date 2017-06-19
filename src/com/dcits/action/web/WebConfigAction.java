package com.dcits.action.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dcits.bean.user.User;
import com.dcits.bean.web.WebConfig;
import com.dcits.service.web.WebConfigService;
import com.dcits.util.StrutsMaps;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Controller
public class WebConfigAction extends ActionSupport implements ModelDriven<WebConfig>{

	private static final long serialVersionUID = 1L;

	private WebConfig config=new WebConfig();
	
	@Autowired
	private WebConfigService service;
	
	
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	public String get(){
		User user=(User) StrutsMaps.getSessionMap().get("user");
		config=service.findConfig(user.getUserId());
		if(config==null){
			config=service.findConfig(0);
			WebConfig config1=new WebConfig(user.getUserId(),config.getElementWaitTime(),config.getResultWaitTime(),config.getIePath(),config.getChromePath(),config.getFirefoxPath(),config.getOperaPath(),config.getWindowSize(),config.getErrorInterruptFlag());
			service.editConfig(config1);
		}
		jsonMap.put("config", config);
		jsonMap.put("returnCode", 0);
		
		return SUCCESS;
	}
	
	public String edit(){
		service.editConfig(config);
		jsonMap.put("config", config);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	
	
	
	////////////////////////////////////GET-SET/////////////////////////////////////////////////
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}
	
	@JSON(serialize=false)
	@Override
	public WebConfig getModel() {
		// TODO Auto-generated method stub
		return config;
	}
	
	
	
}
