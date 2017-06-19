package com.dcits.bean.web;

import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

public class WebStep {
	private Integer stepId;
	private Integer orderNum;
	private WebCase webCase;
	private String stepDesc;
	private String stepMethod;
	private WebObject webObject;
	private String callMethod;
	private String requireParameter;
	private String requireValue;
	private String requireParameterType;
	private String capture;
	
	
	private Set<WebReport> reports=new HashSet<WebReport>();
	
	
	public WebStep() {
		super();
		// TODO Auto-generated constructor stub
	}
	public WebStep(Integer orderNum, WebCase webCase, String stepDesc, String stepMethod,
			WebObject webObject, String callMethod, String requireParameter, String requireValue, String requireParameterType, String capture) {
		super();
		this.orderNum = orderNum;
		this.webCase = webCase;
		this.stepDesc = stepDesc;
		this.stepMethod = stepMethod;
		this.webObject = webObject;
		this.callMethod = callMethod;
		this.requireParameter = requireParameter;
		this.requireValue = requireValue;
		this.requireParameterType = requireParameterType;
		this.capture = capture;
	}
	
	
	
	
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
	@JSON(serialize=false)
	public Set<WebReport> getReports() {
		return reports;
	}
	
	public void setReports(Set<WebReport> reports) {
		this.reports = reports;
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
	
	@JSON(serialize=false)
	public WebCase getWebCase() {
		return webCase;
	}
	public void setWebCase(WebCase webCase) {
		this.webCase = webCase;
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
	

	public WebObject getWebObject() {
		return webObject;
	}
	public void setWebObject(WebObject webObject) {
		this.webObject = webObject;
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
	
	
}
