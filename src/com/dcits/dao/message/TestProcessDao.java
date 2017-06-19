package com.dcits.dao.message;

import org.springframework.stereotype.Repository;

import com.dcits.bean.message.TestProcess;

@Repository
public class TestProcessDao extends BaseDao<TestProcess> {

	public void updateProcess(Integer id,String currInfo,String currPercent,String flag){
		String hql="update TestProcess t set t.currentInfo=:currInfo,t.currentProcessPercent=:currPercent,t.completeTag=:flag where t.processId=:id";
		getSession().createQuery(hql).setString("currInfo", currInfo).setString("currPercent",currPercent)
		.setString("flag",flag).setInteger("id",id).executeUpdate();
	}
}
