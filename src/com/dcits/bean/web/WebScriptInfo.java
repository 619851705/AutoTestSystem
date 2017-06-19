package com.dcits.bean.web;

import java.sql.Timestamp;

import org.apache.struts2.json.annotations.JSON;

public class WebScriptInfo {
	
	private Integer scriptId;
	private String scriptName;
	private String scriptPath;
	private String ifPublic;
	private String scriptDesc;
	private String scriptAuthor;
	private Timestamp createTime;
	private Timestamp lastRunTime;
	
	

	public WebScriptInfo(String scriptName, String scriptPath, String ifPublic,
			String scriptDesc, String scriptAuthor, Timestamp createTime,
			Timestamp lastRunTime) {
		super();
		this.scriptName = scriptName;
		this.scriptPath = scriptPath;
		this.ifPublic = ifPublic;
		this.scriptDesc = scriptDesc;
		this.scriptAuthor = scriptAuthor;
		this.createTime = createTime;
		this.lastRunTime = lastRunTime;
	}

	public WebScriptInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	public String getIfPublic() {
		return ifPublic;
	}

	public void setIfPublic(String ifPublic) {
		this.ifPublic = ifPublic;
	}

	public Integer getScriptId() {
		return scriptId;
	}

	public void setScriptId(Integer scriptId) {
		this.scriptId = scriptId;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public String getScriptPath() {
		return scriptPath;
	}

	public void setScriptPath(String scriptPath) {
		this.scriptPath = scriptPath;
	}

	public String getScriptDesc() {
		return scriptDesc;
	}

	public void setScriptDesc(String scriptDesc) {
		this.scriptDesc = scriptDesc;
	}

	public String getScriptAuthor() {
		return scriptAuthor;
	}

	public void setScriptAuthor(String scriptAuthor) {
		this.scriptAuthor = scriptAuthor;
	}

	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Timestamp getLastRunTime() {
		return lastRunTime;
	}

	public void setLastRunTime(Timestamp lastRunTime) {
		this.lastRunTime = lastRunTime;
	}
	
	
	
}
