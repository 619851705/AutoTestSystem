package com.dcits.dao.message;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.bean.message.Parameter;
@SuppressWarnings("unchecked")
@Repository
public class ParameterDao extends BaseDao<Parameter> {
	
	/**
	 * ����interfaceId�������
	 * @param interfaceId
	 * @return
	 */
	public List<Parameter> findAll(int interfaceId){
		return getSession().createQuery("from Parameter where interfaceInfo.interfaceId= :interfaceId").setInteger("interfaceId", interfaceId).list();
	}

	/**
	 * ����ĳһ������ֵ
	 * @param parameterId
	 * @param attrName
	 * @param attrValue
	 */
	public void edit(int parameterId,String attrName,String attrValue) {
		String hsql = "update Parameter p set "+attrName+"= :attrValue where p.parameterId= :parameterId";
		getSession().createQuery(hsql).setString("attrValue", attrValue).setInteger("parameterId",parameterId).executeUpdate();		
	}
	
	
	
}
