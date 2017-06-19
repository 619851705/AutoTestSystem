package com.dcits.dao.message;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.bean.message.TestData;
@SuppressWarnings("unchecked")
@Repository
public class TestDataDao extends BaseDao<TestData> {
	
	//更新单个属性
	public void updateDataValue(Integer dataId,String dataName,String dataValue){
		String hql="update TestData set "+dataName+"= :dataValue where dataId= :dataId";
		getSession().createQuery(hql).setString("dataValue", dataValue).setInteger("dataId",dataId).executeUpdate();
	}
	
	//根据数据标识查找数据
	public List<TestData> findDataByDiscr(String discr,Integer messageSceneId){
		String hql="From TestData t where t.dataDiscr= :discr and t.messageScene.messageSceneId=:messageSceneId and t.status='0'";
		return  getSession().createQuery(hql).setString("discr",discr).setInteger("messageSceneId",messageSceneId).list();
	}
}
