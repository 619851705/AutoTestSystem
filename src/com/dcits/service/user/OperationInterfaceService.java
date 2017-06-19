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
	 * ��ȡ��ǰ���еĲ����ӿڽڵ�
	 * @return
	 */
	public List<OperationInterface> getNodes(){
		return dao.findAll();
	}
	
	/**
	 * ��ȡָ���Ĳ����ӿ���Ϣ
	 * @param opId
	 * @return
	 */
	public OperationInterface get(Integer opId){
		return dao.get(opId);
	}
}
