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
	 * 获取一个接口的所有入参
	 * @param interfaceId
	 * @return
	 */
	public List<Parameter> list(int interfaceId){
		return parameterDao.findAll(interfaceId);
	}
	
	/**
	 * 删除一个参数
	 * @param parameterId
	 */
	public void delParameter(int parameterId){
		parameterDao.delete(parameterId);
	}
	/**
	 * 更新参数某一个属性
	 * @param parameterId
	 * @param attrName
	 * @param attrValue
	 */
	public void editParameter(int parameterId,String attrName,String attrValue){
		parameterDao.edit(parameterId, attrName, attrValue);
	}
	/**
	 * 保存一个参数,返回数据库生成的主键
	 * @param parameter
	 * @return
	 */
	public Integer saveParameter(Parameter parameter){
		return parameterDao.save(parameter);
	}
}
