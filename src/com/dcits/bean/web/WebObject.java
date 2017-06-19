package com.dcits.bean.web;

import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

public class WebObject {
	private Integer objectId;
	private String objectName;
	private String objectType;
	private String how;
	private String propertyValue;
	private String pageUrl;
	private Integer objectSeq;
	
	private WebObjectCategory webObjectCategory;
	
	private Set<WebStep> steps=new HashSet<WebStep>();
	private Set<WebStepPublic> stepsP=new HashSet<WebStepPublic>();
	
	
	public WebObject() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public WebObject(String objectName, String objectType, String how, String propertyValue,
			String pageUrl,Integer objectSeq,WebObjectCategory webObjectCategory) {
		super();
		this.objectName = objectName;
		this.objectType = objectType;
		this.how = how;
		this.propertyValue = propertyValue;
		this.pageUrl = pageUrl;
		this.objectSeq =  objectSeq;
		this.webObjectCategory = webObjectCategory;
	}
	
	
	
	@JSON(serialize=false)
	public Set<WebStepPublic> getStepsP() {
		return stepsP;
	}

	public void setStepsP(Set<WebStepPublic> stepsP) {
		this.stepsP = stepsP;
	}

	@JSON(serialize=false)
	public WebObjectCategory getWebObjectCategory() {
		return webObjectCategory;
	}

	public void setWebObjectCategory(WebObjectCategory webObjectCategory) {
		this.webObjectCategory = webObjectCategory;
	}

	public Integer getObjectSeq() {
		return objectSeq;
	}

	public void setObjectSeq(Integer objectSeq) {
		this.objectSeq = objectSeq;
	}

	@JSON(serialize=false)
	public Set<WebStep> getSteps() {
		return steps;
	}

	public void setSteps(Set<WebStep> steps) {
		this.steps = steps;
	}

	public Integer getObjectId() {
		return objectId;
	}
	public void setObjectId(Integer objectId) {
		this.objectId = objectId;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public String getHow() {
		return how;
	}
	public void setHow(String how) {
		this.how = how;
	}
	public String getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	public String getPageUrl() {
		return pageUrl;
	}
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	
	
	
}
