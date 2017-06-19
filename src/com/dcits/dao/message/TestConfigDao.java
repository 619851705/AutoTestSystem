package com.dcits.dao.message;

import org.springframework.stereotype.Repository;

import com.dcits.bean.message.TestConfig;

@Repository
public class TestConfigDao extends BaseDao<TestConfig>{
	
	public TestConfig getConfigByUserId(Integer userId){
		String hql="From TestConfig t where t.userId= :userId";
		return (TestConfig) getSession().createQuery(hql).setInteger("userId",userId).uniqueResult();
	}
}
