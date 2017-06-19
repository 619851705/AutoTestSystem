package com.dcits.dao.message;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.bean.message.MessageScene;
@SuppressWarnings("unchecked")
@Repository
public class MessageSceneDao extends BaseDao<MessageScene> {

	
	public List<MessageScene> findByMessageId(Integer messageId) {
		String hsql="From MessageScene m where m.message.messageId= :messageId";
		return getSession().createQuery(hsql).setInteger("messageId", messageId).list();
	}

	@Override
	public List<MessageScene> findAll() {
		String hql="From MessageScene m where m.message.status='0' and m.message.interfaceInfo.status='0'";
		return getSession().createQuery(hql).list();
	}
	
	public void updateValidateFlag(Integer messageSceneId,String validateRuleFlag){
		String hql = "update MessageScene m set m.validateRuleFlag=:validateRuleFlag where m.messageSceneId=:messageSceneId";
		getSession().createQuery(hql).setString("validateRuleFlag",validateRuleFlag).setInteger("messageSceneId",messageSceneId).executeUpdate();
		
	}
}
