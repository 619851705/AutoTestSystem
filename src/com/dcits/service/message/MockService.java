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
	 * 根据ID查找指定mock接口
	 * @param mockId
	 * @return
	 */
	public InterfaceMock get(Integer mockId){
		return mockDao.get(mockId);
	}
	
	/**
	 * 保存新的mock信息
	 * @param mock
	 * @return
	 */
	public void edit(InterfaceMock mock){
		mockDao.edit(mock);
	}
	
	/**
	 * 查找指定接口的mock信息
	 * @param interfaceName
	 * @return
	 */
	public InterfaceMock findByName(String interfaceName){
		return mockDao.findByName(interfaceName);
	}
	
	/**
	 * 更新调用次数
	 * @param mockId
	 */
	public void updateCallCount(Integer mockId){
		mockDao.updateCallCount(mockId);
	}
	
	/**
	 * 查找所以mock信息
	 * @return
	 */
	public List<InterfaceMock> findAll(){
		return mockDao.findAll();
	}
	
	/**
	 * 删除mock信息
	 * @param mockId
	 */
	public void delMock(Integer mockId){
		mockDao.delete(mockId);
	}
}
