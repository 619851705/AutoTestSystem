package com.dcits.dao.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.bean.user.UserMail;
import com.dcits.dao.message.BaseDao;

@Repository
public class UserMailDao extends BaseDao<UserMail>{
	
	public int getNoReadNum(Integer receiveUserId){
		String hql = "select count(*) from UserMail m where m.receiveUser.userId=:receiveUserId and m.readStatus='1' and m.sendStatus='0'";
		return ((Number)getSession().createQuery(hql).setInteger("receiveUserId", receiveUserId).uniqueResult()).intValue();
	}
	
	@SuppressWarnings("unchecked")
	public List<UserMail> findReadMails(Integer receiveUserId){
		String hql = "From UserMail m where m.receiveUser.userId=:receiveUserId and m.sendStatus='0'";
		return getSession().createQuery(hql).setInteger("receiveUserId", receiveUserId).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<UserMail> findSendMails(Integer sendUserId){
		String hql = "From UserMail m where m.sendUser.userId=:sendUserId";
		return getSession().createQuery(hql).setInteger("sendUserId", sendUserId).list();
	}
	
	public void changeStatus(Integer mailId,String statusName,String status){
		String hql = "update UserMail set "+statusName+"=:status where mailId=:mailId";
		getSession().createQuery(hql).setString("status",status).setInteger("mailId", mailId).executeUpdate();
	}
	
}
