package com.dcits.dao.message;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dcits.bean.message.InterfaceInfo;
import com.dcits.util.PracticalUtils;

@Repository
@SuppressWarnings("unchecked")
public class InterfaceInfoDao extends BaseDao<InterfaceInfo> {

	
	@Override
	public List<InterfaceInfo> findAll() {
		String hsql="from InterfaceInfo t where t.status <> '2'";		
		return getSession().createQuery(hsql).list();
	}
	
	public List<InterfaceInfo> findInterfaceByCondition(String condition){
		String hql = "";
		if(PracticalUtils.isNumeric(condition)){
			hql = "From InterfaceInfo t where t.interfaceId=:id or t.interfaceName like :name or t.interfaceCnName like :cnName";
			return getSession().createQuery(hql).setInteger("id",Integer.parseInt(condition)).setString("name","%"+condition+"%").setString("cnName", "%"+condition+"%").list();
		}else{
			hql = "From InterfaceInfo t where t.interfaceName like :name or t.interfaceCnName like :cnName";
			return getSession().createQuery(hql).setString("name","%"+condition+"%").setString("cnName", "%"+condition+"%").list();
		}
	}
}
