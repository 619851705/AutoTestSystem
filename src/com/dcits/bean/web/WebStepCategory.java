package com.dcits.bean.web;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

public class WebStepCategory {
	
	private Integer categoryId;
	private String categoryName;
	private String categoryDesc;
	private String createUser;
	private Timestamp submitTime;
	private Timestamp handleTime;
	private String categoryTag;
	private Integer useCount;
	private String status;
	
	private Set<WebStepPublic> steps=new HashSet<WebStepPublic>();
	private Integer stepNum;
	
	
	
	public Integer getStepNum() {
		return stepNum;
	}

	public void setStepNum() {
		this.stepNum = steps.size();
	}

	@JSON(serialize=false)
	public Set<WebStepPublic> getSteps() {
		return steps;
	}
	
	public void setSteps(Set<WebStepPublic> steps) {
		this.steps = steps;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryDesc() {
		return categoryDesc;
	}
	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Timestamp getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(Timestamp submitTime) {
		this.submitTime = submitTime;
	}
	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Timestamp getHandleTime() {
		return handleTime;
	}
	public void setHandleTime(Timestamp handleTime) {
		this.handleTime = handleTime;
	}
	public String getCategoryTag() {
		return categoryTag;
	}
	public void setCategoryTag(String categoryTag) {
		this.categoryTag = categoryTag;
	}
	public Integer getUseCount() {
		return useCount;
	}
	public void setUseCount(Integer useCount) {
		this.useCount = useCount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public WebStepCategory() {
		super();
		// TODO Auto-generated constructor stub
	}
	public WebStepCategory(String categoryName, String categoryDesc,
			String createUser, Timestamp submitTime, String categoryTag,
			Integer useCount, String status) {
		super();
		this.categoryName = categoryName;
		this.categoryDesc = categoryDesc;
		this.createUser = createUser;
		this.submitTime = submitTime;
		this.categoryTag = categoryTag;
		this.useCount = useCount;
		this.status = status;
	}
	
	
	
}
