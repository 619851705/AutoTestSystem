package com.dcits.dao.web;

import org.springframework.stereotype.Repository;

import com.dcits.bean.web.WebConfig;
import com.dcits.dao.message.BaseDao;

@Repository
public class WebConfigDao extends BaseDao<WebConfig>{
	
	public WebConfig findByUserId(Integer userId){
		String hql="From WebConfig w where w.userId=:userId";
		return (WebConfig) getSession().createQuery(hql).setInteger("userId",userId).uniqueResult();
	}
}
