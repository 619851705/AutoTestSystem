package com.dcits.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.user.OperationInterface;
import com.dcits.dao.user.OperationInterfaceDao;

@Service
public class OperationInterfaceService {
	@Autowired
	private OperationInterfaceDao dao;
	
	
	/**
	 * 获取当前所有的操作接口节点
	 * @return
	 */
	public List<OperationInterface> getNodes(){
		return dao.findAll();
	}
	
	/**
	 * 获取指定的操作接口信息
	 * @param opId
	 * @return
	 */
	public OperationInterface get(Integer opId){
		return dao.get(opId);
	}
}
