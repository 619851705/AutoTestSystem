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
	 * 获取所有接口列表
	 * @return
	 */
	public List<InterfaceInfo> findAllInterfaces(){
		return interfaceInfoDao.findAll();
	}
	
	/**
	 * 删除接口
	 * 会删除接口下的所有内容,包括参数、数据、报文等,所以一般删除只将接口的状态改为2,只有admin操作才能执行真正的删除
	 * @param id
	 */
	public void delInterface(int id){
		interfaceInfoDao.delete(id);
	}
	
	/**
	 * 改变状态 
	 * @param id
	 */
	public void changeStatus(int id,String status){
		String hsql="update InterfaceInfo t set t.status='"+status+"' where t.interfaceId="+id;
		interfaceInfoDao.update(hsql);
	}
	
	/**
	 * 根据id获取interfaceInfo
	 * @param id
	 * @return
	 */
	public InterfaceInfo getInterfaceInfoById(int id){
		return interfaceInfoDao.get(id);
	}
	
	/**
	 * 保存一个新的接口
	 * @param entity
	 */
	public void saveInterfaceInfo(InterfaceInfo entity){
		interfaceInfoDao.save(entity);
	}
	
	/**
	 * 根据接口名寻找接口
	 * @param interfaceName
	 * @return
	 */
	public InterfaceInfo findInterfaceByName(String interfaceName){
		String hsql="From InterfaceInfo i where i.interfaceName='"+interfaceName+"'";
		return interfaceInfoDao.findUnique(hsql);
	}
	/**
	 * 更新接口信息
	 * @param p
	 */
	public void updateInterface(InterfaceInfo p){
		interfaceInfoDao.edit(p);
	}
	
	/**
	 * 根据条件模糊查询接口
	 * @param condition
	 * @return
	 */
	public List<InterfaceInfo> findInterfaceByCondition(String condition){
		return interfaceInfoDao.findInterfaceByCondition(condition);
	}
}
