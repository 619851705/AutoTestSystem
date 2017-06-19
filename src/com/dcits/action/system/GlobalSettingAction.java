package com.dcits.action.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dcits.bean.system.GlobalSetting;
import com.dcits.service.system.GlobalSettingService;
import com.dcits.util.StrutsMaps;
import com.opensymphony.xwork2.ActionSupport;

@Controller
public class GlobalSettingAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	@Autowired
	private GlobalSettingService service;
	
	//ajax调用返回的map
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	
	//获取当前设置信息
	@SuppressWarnings("unchecked")
	public String getWebSettings(){
		Map<String,GlobalSetting> settingMap = (Map<String, GlobalSetting>) StrutsMaps.getApplicationMap().get("settingMap");
		
		for(GlobalSetting setting:settingMap.values()){
			jsonMap.put(setting.getSettingName(), setting.getSettingValue()==null?setting.getDefaultValue():setting.getSettingValue());
		}
		jsonMap.put("returnCode", 0);
		return SUCCESS;
		
	}
	
	//获取列表
	public String list(){
		List<GlobalSetting> settings = service.findAll();
		for(GlobalSetting g:settings){
			if(g.getSettingValue()==null){
				g.setSettingValue("");
			}
		}
		jsonMap.put("data", settings);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//编辑
	public String edit(){
		for(Map.Entry<String, Object> entry:StrutsMaps.getParametersMap().entrySet()){
			service.updateSetting(entry.getKey(), ((String[])entry.getValue())[0]);
		}
		List<GlobalSetting> settings = service.findAll();
		Map<String,GlobalSetting> globalSettingMap = new HashMap<String,GlobalSetting>();
		for(GlobalSetting g:settings){
			globalSettingMap.put(g.getSettingName(), g);
		}
		StrutsMaps.getApplicationMap().put("settingMap", globalSettingMap);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}
	
}
