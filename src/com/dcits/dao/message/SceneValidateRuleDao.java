package com.dcits.dao.message;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.bean.message.SceneValidateRule;

@Repository
public class SceneValidateRuleDao extends BaseDao<SceneValidateRule>{
	
	public SceneValidateRule getFullValidate(Integer messageSceneId){
		String hql = "From SceneValidateRule s where s.messageScene.messageSceneId=:messageSceneId and s.fullValidateFlag='0'";
		return (SceneValidateRule) getSession().createQuery(hql).setInteger("messageSceneId", messageSceneId).uniqueResult();
	}

	public void updateValidateValue(Integer validateId,String validateValue){
		String hql = "update SceneValidateRule s set s.validateValue=:validateValue where s.validateId=:validateId";
		getSession().createQuery(hql).setString("validateValue", validateValue).setInteger("validateId",validateId).executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<SceneValidateRule> getParameterValidate(Integer messageSceneId){
		String hql = "From SceneValidateRule s where s.messageScene.messageSceneId=:messageSceneId and s.fullValidateFlag<>'0'";
		return  getSession().createQuery(hql).setInteger("messageSceneId", messageSceneId).list();
	}
	
	public void updateStatus(Integer validateId,String status){
		String hql = "update SceneValidateRule s set s.status=:status where s.validateId=:validateId";
		getSession().createQuery(hql).setString("status", status).setInteger("validateId",validateId).executeUpdate();
	}
	
	

}
