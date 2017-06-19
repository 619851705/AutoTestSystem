package com.dcits.bean.web;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

import com.dcits.bean.user.User;

public class WebCaseSet {
	
	private Integer setId;
	private String setName;
	private String setDesc;
	private Integer testCount;
	private User user;
	private Timestamp createTime;
	private String lastModifyUser;
	private String status;
	
	private String createUser;
	private String caseNum;
	
	private Set<WebCaseSetComp> comps=new HashSet<WebCaseSetComp>();
	
	private Set<WebReportSet> reportSets=new HashSet<WebReportSet>();
	
	
	
	
	
	public String getCaseNum() {
		return caseNum;
	}

	@JSON(serialize=false)
	public Set<WebReportSet> getReportSets() {
		return reportSets;
	}

	public void setReportSets(Set<WebReportSet> reportSets) {
		this.reportSets = reportSets;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser() {
		this.createUser = this.user.getRealName();
	}

	@JSON(serialize=false)
	public Set<WebCaseSetComp> getComps() {
		return comps;
	}

	public Set<WebCaseSetComp> getEnableComps(){
		Set<WebCaseSetComp> eComps=new HashSet<WebCaseSetComp>();
		for(WebCaseSetComp comp:this.getComps()){
			if(comp.getStatus().equals("0")){
				eComps.add(comp);
			}
		}
		return eComps;
	}
	
	public void setComps(Set<WebCaseSetComp> comps) {
		this.comps = comps;
	}

	public Integer getSetId() {
		return setId;
	}

	public void setSetId(Integer setId) {
		this.setId = setId;
	}

	public String getSetName() {
		return setName;
	}

	public void setSetName(String setName) {
		this.setName = setName;
	}

	public String getSetDesc() {
		return setDesc;
	}

	public void setSetDesc(String setDesc) {
		this.setDesc = setDesc;
	}

	public Integer getTestCount() {
		return testCount;
	}

	public void setTestCount(Integer testCount) {
		this.testCount = testCount;
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

	public String getLastModifyUser() {
		return lastModifyUser;
	}

	public void setLastModifyUser(String lastModifyUser) {
		this.lastModifyUser = lastModifyUser;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public WebCaseSet(String setName, String setDesc, Integer testCount,
			User user, Timestamp createTime, String lastModifyUser,
			String status) {
		super();
		this.setName = setName;
		this.setDesc = setDesc;
		this.testCount = testCount;
		this.user = user;
		this.createTime = createTime;
		this.lastModifyUser = lastModifyUser;
		this.status = status;
	}

	public WebCaseSet() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
