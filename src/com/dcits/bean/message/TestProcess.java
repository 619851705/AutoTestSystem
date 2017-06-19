package com.dcits.bean.message;


public class TestProcess {	
	private Integer processId;
	private Integer reportId;
	private Integer currentProcessPercent;
	private String currentInfo;
	private String completeTag;
	
	public TestProcess(Integer reportId, String completeTag) {
		super();
		this.reportId = reportId;
		this.completeTag = completeTag;
	}

	public TestProcess() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getProcessId() {
		return processId;
	}

	public void setProcessId(Integer processId) {
		this.processId = processId;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public Integer getCurrentProcessPercent() {
		return currentProcessPercent;
	}

	public void setCurrentProcessPercent(Integer currentProcessPercent) {
		this.currentProcessPercent = currentProcessPercent;
	}

	public String getCurrentInfo() {
		return currentInfo;
	}

	public void setCurrentInfo(String currentInfo) {
		this.currentInfo = currentInfo;
	}

	public String getCompleteTag() {
		return completeTag;
	}

	public void setCompleteTag(String completeTag) {
		this.completeTag = completeTag;
	}
	
	
	
}
