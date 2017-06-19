package com.dcits.dao.web;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.bean.web.WebCaseSetComp;
import com.dcits.dao.message.BaseDao;
@SuppressWarnings("unchecked")
@Repository
public class WebCaseSetCompDao extends BaseDao<WebCaseSetComp> {
	
	public WebCaseSetComp get(Integer caseId,Integer setId){
		String hql="From WebCaseSetComp w where w.webCaseSet.setId=:setId and w.webCase.caseId=:caseId";
		return (WebCaseSetComp) getSession().createQuery(hql).setInteger("caseId",caseId).setInteger("setId",setId).uniqueResult();
	}
	
	public List<WebCaseSetComp> findByUserId(Integer userId){
		String hql="From WebCaseSetComp w where w.user.userId=:userId";
		return getSession().createQuery(hql).setInteger("userId",userId).list();
	}
	
	public void updateStatus(Integer id,String status){
		String hql="Update WebCaseSetComp w set w.status=:status,w.submitTime=:submitTime where w.id=:id";
		getSession().createQuery(hql).setString("status",status).setInteger("id",id).setTimestamp("submitTime", new Timestamp(System.currentTimeMillis())).executeUpdate();
	}
	
	public List<WebCaseSetComp> findAll(Integer setId){
		String hql="From WebCaseSetComp w where w.webCaseSet.setId=:setId and (w.status='0' or w.status='1')";
		return getSession().createQuery(hql).setInteger("setId",setId).list();
	}

}
