package com.dcits.dao.web;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.bean.web.WebScriptInfo;
import com.dcits.dao.message.BaseDao;

@Repository
public class WebScriptInfoDao extends BaseDao<WebScriptInfo> {

	//type=0  �����ű�  type=1 �ǹ����ű�
	@SuppressWarnings("unchecked")
	public List<WebScriptInfo> findAll(String type) {
		String hql = "From WebScriptInfo w";
		if(type.equals("0")||type.equals("1")){
			hql+=" where w.ifPublic='"+type+"'";
		}
		return getSession().createQuery(hql).list();
	}
	
	
	//���½ű������һ������ʱ��
	public void updateRunTime(Integer id){
		String hql = "update WebScriptInfo s set s.lastRunTime=:lastRunTime where s.scriptId=:scriptId";
		getSession().createQuery(hql).setTimestamp("lastRunTime", new Timestamp(System.currentTimeMillis())).setInteger("scriptId", id).executeUpdate();
		
	}

}
