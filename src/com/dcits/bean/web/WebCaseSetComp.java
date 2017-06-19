package com.dcits.bean.web;

import java.sql.Timestamp;

import org.apache.struts2.json.annotations.JSON;

import com.dcits.bean.user.User;

public class WebCaseSetComp {
	
	private Integer id;
	private WebCaseSet webCaseSet;
	private WebCase webCase;
	private String status;
	private User user;
	private Timestamp submitTime;
	
	private String setName;
	private String caseName;
	private String username;
	private Integer caseId;
	
	public WebCaseSetComp(WebCaseSet webCaseSet, WebCase webCase,
			String status, User user, Timestamp submitTime) {
		super();
		this.webCaseSet = webCaseSet;
		this.webCase = webCase;
		this.status = status;
		this.user = user;
		this.submitTime = submitTime;
	}

	public WebCaseSetComp() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	
	public String getUsername() {
		return username;
	}

	public void setUsername() {
		this.username = this.user.getRealName();
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId() {
		this.caseId = this.webCase.getCaseId();
	}

	public String getSetName() {
		return setName;
	}

	public void setSetName() {
		this.setName = this.webCaseSet.getSetName();
	}

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName() {
		this.caseName = this.webCase.getCaseName();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@JSON(serialize=false)
	public WebCaseSet getWebCaseSet() {
		return webCaseSet;
	}

	public void setWebCaseSet(WebCaseSet webCaseSet) {
		this.webCaseSet = webCaseSet;
	}
	
	@JSON(serialize=false)
	public WebCase getWebCase() {
		return webCase;
	}

	public void setWebCase(WebCase webCase) {
		this.webCase = webCase;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@JSON(serialize=false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Timestamp getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Timestamp submitTime) {
		this.submitTime = submitTime;
	}
	
	
	
}
