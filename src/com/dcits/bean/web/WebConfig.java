package com.dcits.bean.web;

public class WebConfig {

	private Integer configId;
	private Integer userId;
	private Integer elementWaitTime;
	private Integer resultWaitTime;
	private String iePath;
	private String chromePath;
	private String firefoxPath;
	private String operaPath;
	private String windowSize;
	private String errorInterruptFlag;
	public Integer getConfigId() {
		return configId;
	}
	public void setConfigId(Integer configId) {
		this.configId = configId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getElementWaitTime() {
		return elementWaitTime;
	}
	public void setElementWaitTime(Integer elementWaitTime) {
		this.elementWaitTime = elementWaitTime;
	}
	public Integer getResultWaitTime() {
		return resultWaitTime;
	}
	public void setResultWaitTime(Integer resultWaitTime) {
		this.resultWaitTime = resultWaitTime;
	}
	public String getIePath() {
		return iePath;
	}
	public void setIePath(String iePath) {
		this.iePath = iePath;
	}
	public String getChromePath() {
		return chromePath;
	}
	public void setChromePath(String chromePath) {
		this.chromePath = chromePath;
	}
	public String getFirefoxPath() {
		return firefoxPath;
	}
	public void setFirefoxPath(String firefoxPath) {
		this.firefoxPath = firefoxPath;
	}
	public String getOperaPath() {
		return operaPath;
	}
	public void setOperaPath(String operaPath) {
		this.operaPath = operaPath;
	}
	public String getWindowSize() {
		return windowSize;
	}
	public void setWindowSize(String windowSize) {
		this.windowSize = windowSize;
	}
	public String getErrorInterruptFlag() {
		return errorInterruptFlag;
	}
	public void setErrorInterruptFlag(String errorInterruptFlag) {
		this.errorInterruptFlag = errorInterruptFlag;
	}
	public WebConfig() {
		super();
		// TODO Auto-generated constructor stub
	}
	public WebConfig(Integer userId, Integer elementWaitTime,
			Integer resultWaitTime, String iePath, String chromePath,
			String firefoxPath, String operaPath, String windowSize,
			String errorInterruptFlag) {
		super();
		this.userId = userId;
		this.elementWaitTime = elementWaitTime;
		this.resultWaitTime = resultWaitTime;
		this.iePath = iePath;
		this.chromePath = chromePath;
		this.firefoxPath = firefoxPath;
		this.operaPath = operaPath;
		this.windowSize = windowSize;
		this.errorInterruptFlag = errorInterruptFlag;
	}
	
	
	
}
