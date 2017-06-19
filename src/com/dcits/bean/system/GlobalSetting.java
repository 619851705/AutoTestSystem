package com.dcits.bean.system;

public class GlobalSetting {
	
	private Integer settingId;
	private String settingName;
	private String defaultValue;
	private String settingValue;
	private String mark;
	
	public GlobalSetting(String settingName, String defaultValue,
			String settingValue, String mark) {
		super();
		this.settingName = settingName;
		this.defaultValue = defaultValue;
		this.settingValue = settingValue;
		this.mark = mark;
	}

	public GlobalSetting() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getSettingId() {
		return settingId;
	}

	public void setSettingId(Integer settingId) {
		this.settingId = settingId;
	}

	public String getSettingName() {
		return settingName;
	}

	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getSettingValue() {
		return settingValue;
	}

	public void setSettingValue(String settingValue) {
		this.settingValue = settingValue;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
	
	
}
