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
	 * ����validate������ָ����֤����
	 * @param validateId
	 * @return
	 */
	public SceneValidateRule get(Integer validateId){
		return dao.get(validateId);
	}
	
	/**
	 * ��ȡָ��messageSceneId��ȫ����֤����
	 * @param messageSceneId
	 * @return
	 */
	public SceneValidateRule getFullValidate(Integer messageSceneId){
		return dao.getFullValidate(messageSceneId);
	}
	
	/**
	 * ����ָ��Id����֤�����validateValueֵ
	 * @param validateId
	 * @param validateValue
	 */
	public void updateValidateValue(Integer validateId,String validateValue){
		dao.updateValidateValue(validateId, validateValue);
	}
	
	/**
	 * ���»�������һ����֤����
	 * @param validate
	 */
	public void edit(SceneValidateRule validate){
		dao.edit(validate);
	}
	
	/**
	 * ��ȡ������֤����
	 * @param messageSceneId
	 * @return
	 */
	public List<SceneValidateRule> findParameterValidate(Integer messageSceneId){
		return dao.getParameterValidate(messageSceneId);
	}
	
	/**
	 * ����ָ��validateRule��ָ����status״̬
	 * @param validateId
	 * @param status
	 */
	public void updateStatus(Integer validateId,String status){
		dao.updateStatus(validateId, status);
	}
	
	/**
	 * ɾ��ָ��validateRule
	 * @param validateId
	 */
	public void delValidate(Integer validateId){
		dao.delete(validateId);
	}

}
