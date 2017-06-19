package com.dcits.bean.web;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import com.dcits.bean.user.User;

public class WebTestRmi {
	
	private Integer testId;
	private User user;
	private String testMode;
	private Integer relatedId;
	private Timestamp submitTime;
	private Timestamp finishTime;
	private String status;
	private String testMsg;
	
	private String taskName;
		
	
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public Integer getTestId() {
		return testId;
	}
	public void setTestId(Integer testId) {
		this.testId = testId;
	}
	
	@JSON(serialize=false)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getTestMode() {
		return testMode;
	}
	public void setTestMode(String testMode) {
		this.testMode = testMode;
	}
	public Integer getRelatedId() {
		return relatedId;
	}
	public void setRelatedId(Integer relatedId) {
		this.relatedId = relatedId;
	}
	
	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Timestamp getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(Timestamp submitTime) {
		this.submitTime = submitTime;
	}
	
	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Timestamp getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Timestamp finishTime) {
		this.finishTime = finishTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) throws ParseException {
		this.status = status;
		/*获取当日零时零分*/
		Calendar ca = Calendar.getInstance();
        ca.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime())));
        ca.set(Calendar.HOUR_OF_DAY, 12);//此处进行了修改
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        ca.set(Calendar.MILLISECOND, 0);
        ca.set(Calendar.DATE, ca.get(Calendar.DATE)-1);
        //Date today = ca.getTime();
        Timestamp t1 = new Timestamp(ca.getTimeInMillis());
        /**/
		if(this.status.equals("1")&&this.submitTime.getTime()<=t1.getTime()){
			this.status = "5";
		}
	}
	public String getTestMsg() {
		return testMsg;
	}
	public void setTestMsg(String testMsg) {
		this.testMsg = testMsg;
	}
	public WebTestRmi(User user, String testMode, Integer relatedId,
			Timestamp submitTime, String status, String testMsg, String taskName) {
		super();
		this.user = user;
		this.testMode = testMode;
		this.relatedId = relatedId;
		this.submitTime = submitTime;
		this.status = status;
		this.testMsg = testMsg;
		this.taskName = taskName;
	}
	public WebTestRmi() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
