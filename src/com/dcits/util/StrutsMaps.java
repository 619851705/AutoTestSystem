package com.dcits.util;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dcits.bean.system.GlobalSetting;
import com.opensymphony.xwork2.ActionContext;

/**
 * struts2 Action��ȡ����map
 * @author Administrator
 *
 */
public class StrutsMaps{
	@SuppressWarnings("unchecked")
	/*
	 * ��ȡrequestMap
	 */
	public static Map<String,Object> getRequestMap(){
		return (Map<String, Object>)ActionContext.getContext().get("request");
	}
	
	/*
	 * ��ȡsessionMap
	 */
	public static Map<String,Object> getSessionMap(){
		return ActionContext.getContext().getSession();
	}
	
	/*
	 * ��ȡapplicationMap
	 */
	public static Map<String,Object> getApplicationMap(){
		return ActionContext.getContext().getApplication();
	}
	
	/*
	 * ��ȡparameterMap
	 */
	public static Map<String,Object> getParametersMap(){
		//((String[])parameters.get("name"))[0]
		return ActionContext.getContext().getParameters();
	}
	
	/*
	 * ��ȡactionName
	 */
	public static String getActionName(){
		return ActionContext.getContext().getName();
	}
	
	/**
	 * ��ȡspring������
	 * @return
	 */
	public static ApplicationContext getApplicationContext(){
		HttpServletRequest request = ServletActionContext.getRequest();
		ServletContext sc=request.getSession().getServletContext();
		return WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
	}
	
	/**
	 * ��ȡȫ��������
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getSettingValue(String settingKey){
		Map<String,GlobalSetting> settingMap = (Map<String, GlobalSetting>) StrutsMaps.getApplicationMap().get("settingMap");
		GlobalSetting setting = settingMap.get(settingKey);
		if(setting!=null){
			return setting.getSettingValue()==null?setting.getDefaultValue():setting.getSettingValue();
		}else{
			return null;
		}
		
	}
	
	/**
	 * ��ȡ��Ŀ��·��
	 * @return
	 */
	public static String getProjectPath(){
		ActionContext ac = ActionContext.getContext();
        ServletContext sc = (ServletContext) ac.get(ServletActionContext.SERVLET_CONTEXT);
        return sc.getRealPath("");
	}
}
