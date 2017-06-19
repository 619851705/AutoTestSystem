package com.dcits.dao.system;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.bean.system.AutoTask;
import com.dcits.dao.message.BaseDao;

@Repository
public class AutoTaskDao extends BaseDao<AutoTask>{
	
	@SuppressWarnings("unchecked")
	public List<AutoTask> findRunTasks(){
		String hql = "From AutoTask t where t.status='0'";
		return getSession().createQuery(hql).list();
	}
	
	public void updateStatus(Integer taskId,String status){
		String hql = "update AutoTask t set t.status=:status where t.taskId=:taskId";
		getSession().createQuery(hql).setString("status", status).setInteger("taskId", taskId).executeUpdate();
	}
	
	public void updateCount(Integer taskId,Integer mode){
		String hql = "update AutoTask t set t.runCount=";
		if(mode==0){
			hql+="0";
		}else{
			hql+="t.runCount+1";
		}
		hql+=" where t.taskId=:taskId";
		getSession().createQuery(hql).setInteger("taskId", taskId).executeUpdate();
	}
	
	public void updateExpression(Integer taskId,String expression){
		String hql = "update AutoTask t set t.taskCronExpression=:expression where t.taskId=:taskId";
		getSession().createQuery(hql).setString("expression", expression).setInteger("taskId",taskId).executeUpdate();
	}
}
