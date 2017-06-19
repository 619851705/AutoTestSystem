package com.dcits.bean.web;

import java.sql.Timestamp;

import org.apache.struts2.json.annotations.JSON;

import com.dcits.bean.user.User;

public class WebScriptReport {
	
	private Integer reportId;
	private String reportName;
	private String reportPath;
	private Integer caseNum;
	private User user;
	private Timestamp testTime;
	private String testMark;
	
	private String testUserName;
	
	
	
	public WebScriptReport(String reportName, String reportPath,
			Integer caseNum, User user, Timestamp testTime, String testMark) {
		super();
		this.reportName = reportName;
		this.reportPath = reportPath;
		this.caseNum = caseNum;
		this.user = user;
		this.testTime = testTime;
		this.testMark = testMark;
	}
	
	
	
	
	public String getTestUserName() {
		return testUserName;
	}




	public void setTestUserName() {
		this.testUserName = this.getUser().getRealName();
	}




	public Integer getCaseNum() {
		return caseNum;
	}



	public void setCaseNum(Integer caseNum) {
		this.caseNum = caseNum;
	}



	public WebScriptReport() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getReportId() {
		return reportId;
	}
	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getReportPath() {
		return reportPath;
	}
	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}
	
	@JSON(serialize=false)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Timestamp getTestTime() {
		return testTime;
	}
	public void setTestTime(Timestamp testTime) {
		this.testTime = testTime;
	}
	public String getTestMark() {
		return testMark;
	}
	public void setTestMark(String testMark) {
		this.testMark = testMark;
	}
	
	
	
	
	
}
