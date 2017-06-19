package com.dcits.dao.web;

import org.springframework.stereotype.Repository;

import com.dcits.bean.web.WebCaseSet;
import com.dcits.dao.message.BaseDao;

@Repository
public class WebCaseSetDao extends BaseDao<WebCaseSet> {
	
	public void addTestCount(Integer setId){
		String hql="update WebCaseSet w set w.testCount=w.testCount+1 where w.setId=:setId";
		getSession().createQuery(hql).setInteger("setId",setId).executeUpdate();
	}
}
