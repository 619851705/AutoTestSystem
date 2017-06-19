package com.dcits.service.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.message.InterfaceMock;
import com.dcits.dao.message.MockDao;

@Service
public class MockService {
	
	@Autowired
	private MockDao mockDao;
	
	/**
	 * ����ID����ָ��mock�ӿ�
	 * @param mockId
	 * @return
	 */
	public InterfaceMock get(Integer mockId){
		return mockDao.get(mockId);
	}
	
	/**
	 * �����µ�mock��Ϣ
	 * @param mock
	 * @return
	 */
	public void edit(InterfaceMock mock){
		mockDao.edit(mock);
	}
	
	/**
	 * ����ָ���ӿڵ�mock��Ϣ
	 * @param interfaceName
	 * @return
	 */
	public InterfaceMock findByName(String interfaceName){
		return mockDao.findByName(interfaceName);
	}
	
	/**
	 * ���µ��ô���
	 * @param mockId
	 */
	public void updateCallCount(Integer mockId){
		mockDao.updateCallCount(mockId);
	}
	
	/**
	 * ��������mock��Ϣ
	 * @return
	 */
	public List<InterfaceMock> findAll(){
		return mockDao.findAll();
	}
	
	/**
	 * ɾ��mock��Ϣ
	 * @param mockId
	 */
	public void delMock(Integer mockId){
		mockDao.delete(mockId);
	}
}
