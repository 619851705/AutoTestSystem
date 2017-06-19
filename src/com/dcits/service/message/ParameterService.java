package com.dcits.service.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.message.Parameter;
import com.dcits.dao.message.ParameterDao;

@Service
public class ParameterService {
	
	@Autowired
	private ParameterDao parameterDao;
	
	
	/**
	 * ��ȡһ���ӿڵ��������
	 * @param interfaceId
	 * @return
	 */
	public List<Parameter> list(int interfaceId){
		return parameterDao.findAll(interfaceId);
	}
	
	/**
	 * ɾ��һ������
	 * @param parameterId
	 */
	public void delParameter(int parameterId){
		parameterDao.delete(parameterId);
	}
	/**
	 * ���²���ĳһ������
	 * @param parameterId
	 * @param attrName
	 * @param attrValue
	 */
	public void editParameter(int parameterId,String attrName,String attrValue){
		parameterDao.edit(parameterId, attrName, attrValue);
	}
	/**
	 * ����һ������,�������ݿ����ɵ�����
	 * @param parameter
	 * @return
	 */
	public Integer saveParameter(Parameter parameter){
		return parameterDao.save(parameter);
	}
}
