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
	 * ��ȡ�����е�����
	 * @return
	 */
	public List<AutoTask> findRunTasks(){
		return dao.findRunTasks();
	}
	
	/**
	 * ��ȡ��������
	 * @return
	 */
	public List<AutoTask> findAllTasks(){
		return dao.findAll();
	}
	
	/**
	 * ����id��ȡָ����task
	 * @param taskId
	 * @return
	 */
	public AutoTask get(Integer taskId){
		return dao.get(taskId);
	}
	
	/**
	 * ɾ��ָ����task
	 * @param taskId
	 */
	public void del(Integer taskId){
		dao.delete(taskId);
	}
	
	/**
	 * ����task��״̬ 
	 * �����������ֹͣ����
	 * @param taskId
	 * @param status
	 */
	public void updateStatus(Integer taskId,String status){
		dao.updateStatus(taskId, status);
	}
	
	/**
	 * ���»�������task
	 * @param task
	 */
	public void edit(AutoTask task){
		dao.edit(task);
	}
	
	/**
	 * ����cron���ʽ
	 * @param taskId
	 * @param expression
	 */
	public void updateExpression(Integer taskId,String expression){
		dao.updateExpression(taskId, expression);
	}

}
