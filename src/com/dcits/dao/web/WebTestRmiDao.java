package com.dcits.dao.web;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.bean.web.WebTestRmi;
import com.dcits.dao.message.BaseDao;
@SuppressWarnings("unchecked")
@Repository
public class WebTestRmiDao extends BaseDao<WebTestRmi> {
	
	public List<WebTestRmi> getByUser(Integer userId){
		String hql="From WebTestRmi w where w.user.userId=:userId and w.status='1' and w.submitTime>CURDATE() order by w.submitTime desc";
		return getSession().createQuery(hql).setInteger("userId", userId).list();
	}
	
	public List<WebTestRmi> getUserTask(Integer userId){
		String hql = "From WebTestRmi w where w.user.userId=:userId";
		return getSession().createQuery(hql).setInteger("userId", userId).list();
	}
}
