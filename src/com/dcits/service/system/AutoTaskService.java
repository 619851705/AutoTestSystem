package com.dcits.service.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.system.AutoTask;
import com.dcits.dao.system.AutoTaskDao;

@Service
public class AutoTaskService {
	
	@Autowired
	private AutoTaskDao dao;
	
	
	/**
	 * 获取可运行的任务
	 * @return
	 */
	public List<AutoTask> findRunTasks(){
		return dao.findRunTasks();
	}
	
	/**
	 * 获取所有任务
	 * @return
	 */
	public List<AutoTask> findAllTasks(){
		return dao.findAll();
	}
	
	/**
	 * 根据id获取指定的task
	 * @param taskId
	 * @return
	 */
	public AutoTask get(Integer taskId){
		return dao.get(taskId);
	}
	
	/**
	 * 删除指定的task
	 * @param taskId
	 */
	public void del(Integer taskId){
		dao.delete(taskId);
	}
	
	/**
	 * 更新task的状态 
	 * 开启任务或者停止任务
	 * @param taskId
	 * @param status
	 */
	public void updateStatus(Integer taskId,String status){
		dao.updateStatus(taskId, status);
	}
	
	/**
	 * 更新或者新增task
	 * @param task
	 */
	public void edit(AutoTask task){
		dao.edit(task);
	}
	
	/**
	 * 更新cron表达式
	 * @param taskId
	 * @param expression
	 */
	public void updateExpression(Integer taskId,String expression){
		dao.updateExpression(taskId, expression);
	}

}
