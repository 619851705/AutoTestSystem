package com.dcits.service.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.message.SceneValidateRule;
import com.dcits.dao.message.SceneValidateRuleDao;

@Service
public class SceneValidateRuleService {
	
	@Autowired
	private SceneValidateRuleDao dao;
	
	/**
	 * 根据validate来操作指定验证规则
	 * @param validateId
	 * @return
	 */
	public SceneValidateRule get(Integer validateId){
		return dao.get(validateId);
	}
	
	/**
	 * 获取指定messageSceneId的全文验证规则
	 * @param messageSceneId
	 * @return
	 */
	public SceneValidateRule getFullValidate(Integer messageSceneId){
		return dao.getFullValidate(messageSceneId);
	}
	
	/**
	 * 更新指定Id的验证规则的validateValue值
	 * @param validateId
	 * @param validateValue
	 */
	public void updateValidateValue(Integer validateId,String validateValue){
		dao.updateValidateValue(validateId, validateValue);
	}
	
	/**
	 * 更新或者新增一个验证规则
	 * @param validate
	 */
	public void edit(SceneValidateRule validate){
		dao.edit(validate);
	}
	
	/**
	 * 获取参数验证规则
	 * @param messageSceneId
	 * @return
	 */
	public List<SceneValidateRule> findParameterValidate(Integer messageSceneId){
		return dao.getParameterValidate(messageSceneId);
	}
	
	/**
	 * 更新指定validateRule到指定的status状态
	 * @param validateId
	 * @param status
	 */
	public void updateStatus(Integer validateId,String status){
		dao.updateStatus(validateId, status);
	}
	
	/**
	 * 删除指定validateRule
	 * @param validateId
	 */
	public void delValidate(Integer validateId){
		dao.delete(validateId);
	}

}
