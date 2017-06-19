package com.dcits.bean.web;

import org.apache.struts2.json.annotations.JSON;

public class WebStepPublic {
	private Integer stepId;
	private Integer orderNum;
	private String stepDesc;
	private String stepMethod;
	private String callMethod;
	private String requireParameter;
	private String requireValue;
	private String requireParameterType;
	private String capture;
	
	
	private WebObject webObject;
	private WebStepCategory webStepCategory;
	
	
	
	
	public String getRequireParameter() {
		return requireParameter;
	}
	public void setRequireParameter(String requireParameter) {
		this.requireParameter = requireParameter;
	}

	
	public String getRequireParameterType() {
		return requireParameterType;
	}
	public void setRequireParameterType(String requireParameterType) {
		this.requireParameterType = requireParameterType;
	}
	public Integer getStepId() {
		return stepId;
	}
	public void setStepId(Integer stepId) {
		this.stepId = stepId;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public String getStepDesc() {
		return stepDesc;
	}
	public void setStepDesc(String stepDesc) {
		this.stepDesc = stepDesc;
	}
	public String getStepMethod() {
		return stepMethod;
	}
	public void setStepMethod(String stepMethod) {
		this.stepMethod = stepMethod;
	}
	public String getCallMethod() {
		return callMethod;
	}
	public void setCallMethod(String callMethod) {
		this.callMethod = callMethod;
	}
	public String getRequireValue() {
		return requireValue;
	}
	public void setRequireValue(String requireValue) {
		this.requireValue = requireValue;
	}
	public String getCapture() {
		return capture;
	}
	public void setCapture(String capture) {
		this.capture = capture;
	}
	
	public WebObject getWebObject() {
		return webObject;
	}
	public void setWebObject(WebObject webObject) {
		this.webObject = webObject;
	}
	
	@JSON(serialize=false)
	public WebStepCategory getWebStepCategory() {
		return webStepCategory;
	}
	public void setWebStepCategory(WebStepCategory webStepCategory) {
		this.webStepCategory = webStepCategory;
	}
	public WebStepPublic() {
		super();
		// TODO Auto-generated constructor stub
	}
	public WebStepPublic(Integer orderNum, String stepDesc, String stepMethod,
			String callMethod, String requireParameter,String requireValue, String requireParameterType, String capture,
			WebObject webObject, WebStepCategory webStepCategory) {
		super();
		this.orderNum = orderNum;
		this.stepDesc = stepDesc;
		this.stepMethod = stepMethod;
		this.callMethod = callMethod;
		this.requireParameter = requireParameter;
		this.requireValue = requireValue;
		this.requireParameterType = requireParameterType;
		this.capture = capture;
		this.webObject = webObject;
		this.webStepCategory = webStepCategory;
	}
	
	
	
	
}
