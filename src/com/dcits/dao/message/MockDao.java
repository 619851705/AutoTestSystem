package com.dcits.dao.message;

import org.springframework.stereotype.Repository;

import com.dcits.bean.message.InterfaceMock;

@Repository
public class MockDao extends BaseDao<InterfaceMock> {
	
	public InterfaceMock findByName(String interfaceName){
		String hql="From InterfaceMock m where m.interfaceName= :interfaceName";
		return (InterfaceMock) getSession().createQuery(hql).setString("interfaceName", interfaceName).uniqueResult();
	}
	
	public void updateCallCount(Integer mockId){
		String hql="update InterfaceMock m set m.callCount=m.callCount+1 where m.mockId= :mockId";
		getSession().createQuery(hql).setInteger("mockId", mockId).executeUpdate();
	}
}
