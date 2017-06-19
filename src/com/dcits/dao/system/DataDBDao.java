package com.dcits.dao.system;

import org.springframework.stereotype.Repository;

import com.dcits.bean.system.DataDB;
import com.dcits.dao.message.BaseDao;

@Repository
public class DataDBDao extends BaseDao<DataDB>{
	
	public DataDB getMaxDBId(){
		String hql = "From DataDB d order by d.dbId desc";
		return (DataDB) getSession().createQuery(hql).setFirstResult(0).setMaxResults(1).uniqueResult();
	}
}
