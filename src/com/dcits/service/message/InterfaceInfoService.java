package com.dcits.service.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.message.InterfaceInfo;
import com.dcits.dao.message.InterfaceInfoDao;

@Service
public class InterfaceInfoService {
	
	@Autowired
	private InterfaceInfoDao interfaceInfoDao;
	
	/**
	 * ��ȡ���нӿ��б�
	 * @return
	 */
	public List<InterfaceInfo> findAllInterfaces(){
		return interfaceInfoDao.findAll();
	}
	
	/**
	 * ɾ���ӿ�
	 * ��ɾ���ӿ��µ���������,�������������ݡ����ĵ�,����һ��ɾ��ֻ���ӿڵ�״̬��Ϊ2,ֻ��admin��������ִ��������ɾ��
	 * @param id
	 */
	public void delInterface(int id){
		interfaceInfoDao.delete(id);
	}
	
	/**
	 * �ı�״̬ 
	 * @param id
	 */
	public void changeStatus(int id,String status){
		String hsql="update InterfaceInfo t set t.status='"+status+"' where t.interfaceId="+id;
		interfaceInfoDao.update(hsql);
	}
	
	/**
	 * ����id��ȡinterfaceInfo
	 * @param id
	 * @return
	 */
	public InterfaceInfo getInterfaceInfoById(int id){
		return interfaceInfoDao.get(id);
	}
	
	/**
	 * ����һ���µĽӿ�
	 * @param entity
	 */
	public void saveInterfaceInfo(InterfaceInfo entity){
		interfaceInfoDao.save(entity);
	}
	
	/**
	 * ���ݽӿ���Ѱ�ҽӿ�
	 * @param interfaceName
	 * @return
	 */
	public InterfaceInfo findInterfaceByName(String interfaceName){
		String hsql="From InterfaceInfo i where i.interfaceName='"+interfaceName+"'";
		return interfaceInfoDao.findUnique(hsql);
	}
	/**
	 * ���½ӿ���Ϣ
	 * @param p
	 */
	public void updateInterface(InterfaceInfo p){
		interfaceInfoDao.edit(p);
	}
	
	/**
	 * ��������ģ����ѯ�ӿ�
	 * @param condition
	 * @return
	 */
	public List<InterfaceInfo> findInterfaceByCondition(String condition){
		return interfaceInfoDao.findInterfaceByCondition(condition);
	}
}
