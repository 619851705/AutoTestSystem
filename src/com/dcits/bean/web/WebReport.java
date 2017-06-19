package com.dcits.bean.web;

import java.sql.Timestamp;

import org.apache.struts2.json.annotations.JSON;

public class WebReport {
	private Integer reportId;
	private WebStep webStep;
	private WebReportCase webReportCase;
	private String runStatus;
	private String testMark;
	private String capturePath;
	private String testUsername;
	private Timestamp opTime;
	
	private String stepName;
	
	public WebReport() {
		super();
		// TODO Auto-generated constructor stub
	}
	public WebReport(WebStep webStep, WebReportCase webReportCase,String runStatus,String testMark, String capturePath,
			String testUsername, Timestamp opTime) {
		super();
		this.webStep = webStep;
		this.runStatus = runStatus;
		this.webReportCase = webReportCase;
		this.capturePath = capturePath;
		this.testUsername = testUsername;
		this.opTime = opTime;
		this.testMark = testMark;
	}
		
	
	
	
	public String getStepName() {
		return stepName;
	}
	public void setStepName() {
		this.stepName = this.webStep.getStepDesc();
	}
	public WebReportCase getWebReportCase() {
		return webReportCase;
	}
	public void setWebReportCase(WebReportCase webReportCase) {
		this.webReportCase = webReportCase;
	}
	public String getTestMark() {
		return testMark;
	}
	public void setTestMark(String testMark) {
		this.testMark = testMark;
	}
	public Integer getReportId() {
		return reportId;
	}
	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}
	
	@JSON(serialize=false)
	public WebStep getWebStep() {
		return webStep;
	}
	public void setWebStep(WebStep webStep) {
		this.webStep = webStep;
	}
	public String getRunStatus() {
		return runStatus;
	}
	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}
	public String getCapturePath() {
		return capturePath;
	}
	public void setCapturePath(String capturePath) {
		this.capturePath = capturePath;
	}
	public String getTestUsername() {
		return testUsername;
	}
	public void setTestUsername(String testUsername) {
		this.testUsername = testUsername;
	}
	
	@JSON(format="yyyy-MM-dd hh:mm:ss")
	public Timestamp getOpTime() {
		return opTime;
	}
	public void setOpTime(Timestamp opTime) {
		this.opTime = opTime;
	}

	
	
}
