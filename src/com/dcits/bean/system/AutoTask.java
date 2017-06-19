package com.dcits.bean.system;

import java.sql.Timestamp;

import org.apache.struts2.json.annotations.JSON;

public class AutoTask {
	
	private Integer taskId;
	private String taskName;
	private String taskType;
	private Integer relatedId;
	private String taskCronExpression;
	private Integer runCount;
	private Timestamp lastFinishTime;
	private Timestamp createTime;
	private String status;
	
	private String setName = "";
	
	public AutoTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AutoTask(String taskName, String taskType, Integer relatedId,
			String taskCronExpression, Integer runCount,
			Timestamp lastFinishTime, Timestamp createTime, String status) {
		super();
		this.taskName = taskName;
		this.taskType = taskType;
		this.relatedId = relatedId;
		this.taskCronExpression = taskCronExpression;
		this.runCount = runCount;
		this.lastFinishTime = lastFinishTime;
		this.createTime = createTime;
		this.status = status;
	}

	

	public String getSetName() {
		return setName;
	}

	public void setSetName(String setName) {
		this.setName = setName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public Integer getRelatedId() {
		return relatedId;
	}

	public void setRelatedId(Integer relatedId) {
		this.relatedId = relatedId;
	}

	public String getTaskCronExpression() {
		return taskCronExpression;
	}

	public void setTaskCronExpression(String taskCronExpression) {
		this.taskCronExpression = taskCronExpression;
	}

	public Integer getRunCount() {
		return runCount;
	}

	public void setRunCount(Integer runCount) {
		this.runCount = runCount;
	}

	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Timestamp getLastFinishTime() {
		return lastFinishTime;
	}

	public void setLastFinishTime(Timestamp lastFinishTime) {
		this.lastFinishTime = lastFinishTime;
	}

	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	
}
