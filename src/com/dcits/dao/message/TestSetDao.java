package com.dcits.dao.message;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.bean.message.MessageScene;
import com.dcits.bean.message.TestSet;
@SuppressWarnings("unchecked")
@Repository
public class TestSetDao extends BaseDao<TestSet> {

	@Override
	public List<TestSet> findAll() {
		String hql="From TestSet t where t.status<>'2'";
		return getSession().createQuery(hql).list();
	}
	
	public void changeSetStatus(Integer id,String status){
		String hql="update TestSet t set t.status='"+status+"' where t.setId="+id;
		update(hql);
	}
	
	public TestSet findSetByName(String setName){
		String hql="From TestSet t where t.setName=:setName";
		TestSet t=(TestSet) getSession().createQuery(hql).setString("setName",setName).uniqueResult();
		return t;
	}
	
	//获取所有的场景-当前测试集没有的和报文接口的状态都为0
	public List<MessageScene> getAllScene(Integer setId){
		String hql="From MessageScene m where m.message.status='0' and m.message.interfaceInfo.status='0'";
		List<MessageScene> mss=getSession().createQuery(hql).list();
		List<MessageScene> mss1=new ArrayList<MessageScene>(get(setId).getMs());
		mss.removeAll(mss1);
		return mss;
	} 
	
	public void addScene(Integer messageSceneId,Integer setId){
		String sql="insert into at_set_scene values (:setId,:messageSceneId)";
		getSession().createSQLQuery(sql).setInteger("messageSceneId",messageSceneId).setInteger("setId",setId).executeUpdate();
	}
	
	public List<TestSet> findSetByUser(Integer userId){
		String hql="From TestSet t where t.status='0' and t.user.userId=:userId";
		return getSession().createQuery(hql).setInteger("userId",userId).list();
	}
	
	
}
