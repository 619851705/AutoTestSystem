package com.dcits.bean.web;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

import com.dcits.bean.user.User;



public class WebCase {
	private Integer caseId;
	private String caseName;
	private String caseDesc;
	private String browser;
	private String runFlag;
	private User user;
	private Timestamp createTime;
	
	private Set<WebStep> steps=new HashSet<WebStep>();
	
	private Set<WebReportCase> reportCases=new HashSet<WebReportCase>();
	
	private Set<WebCaseSetComp> comps=new HashSet<WebCaseSetComp>();
	
	private Integer stepNum;
	
	public WebCase() {
		super();
		// TODO Auto-generated constructor stub
	}
	public WebCase(String caseName, String caseDesc, String browser, String runFlag, User user,
			Timestamp createTime) {
		super();
		this.caseName = caseName;
		this.caseDesc = caseDesc;
		this.browser = browser;
		this.runFlag = runFlag;
		this.user = user;
		this.createTime = createTime;
	}
	
	
	
	@JSON(serialize=false)
	public Set<WebCaseSetComp> getComps() {
		return comps;
	}
	public void setComps(Set<WebCaseSetComp> comps) {
		this.comps = comps;
	}
	public Integer getStepNum() {
		return stepNum;
	}
	public void setStepNum() {
		this.stepNum = steps.size();
	}
	
	
	@JSON(serialize=false)
	public Set<WebStep> getSteps() {
		return steps;
	}
	
	@JSON(serialize=false)
	public Set<WebReportCase> getReportCases() {
		return reportCases;
	}
	public void setReportCases(Set<WebReportCase> reportCases) {
		this.reportCases = reportCases;
	}
	
	@SuppressWarnings("unused")
	private void setSteps(Set<WebStep> steps) {
		this.steps = steps;
	}
	
	public void addSteps(Set<WebStep> steps){
		this.steps.clear();
		if(steps!=null&&steps.size()!=0){
			this.steps.addAll(steps);
		}
	}
	
	
	
	public Integer getCaseId() {
		return caseId;
	}
	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	public String getCaseName() {
		return caseName;
	}
	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}
	public String getCaseDesc() {
		return caseDesc;
	}
	public void setCaseDesc(String caseDesc) {
		this.caseDesc = caseDesc;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	public String getRunFlag() {
		return runFlag;
	}
	public void setRunFlag(String runFlag) {
		this.runFlag = runFlag;
	}
	
	@JSON(serialize=false)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	

	
}
