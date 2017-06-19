package com.dcits.bean.system;

public class DataDB {
	
	private Integer dbId;
	private String dbType;
	private String dbUrl;
	private String dbName;
	private String dbUsername;
	private String dbPasswd;
	private String dbMark;
	
	public DataDB(Integer dbId, String dbType, String dbUrl, String dbName,
			String dbUsername, String dbPasswd, String dbMark) {
		super();
		this.dbId = dbId;
		this.dbType = dbType;
		this.dbUrl = dbUrl;
		this.dbName = dbName;
		this.dbUsername = dbUsername;
		this.dbPasswd = dbPasswd;
		this.dbMark = dbMark;
	}

	public DataDB() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getDbId() {
		return dbId;
	}

	public void setDbId(Integer dbId) {
		this.dbId = dbId;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public String getDbPasswd() {
		return dbPasswd;
	}

	public void setDbPasswd(String dbPasswd) {
		this.dbPasswd = dbPasswd;
	}

	public String getDbMark() {
		return dbMark;
	}

	public void setDbMark(String dbMark) {
		this.dbMark = dbMark;
	}
	
	
}
