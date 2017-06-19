package com.dcits.service.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.web.WebObject;
import com.dcits.dao.web.WebObjectDao;

@Service
public class WebObjectService {
	
	@Autowired
	private WebObjectDao webObjectDao;
	
	/**
	 * 查找全部的测试对象
	 * @return
	 */
	public List<WebObject> findAll(){
		return webObjectDao.findAll();
	}
	
	/**
	 * 保存新的测试对象
	 * 修改测试对象
	 * @param o
	 * @return
	 */
	public void editObject(WebObject o){
		webObjectDao.edit(o);
	}
	
	/**
	 * 删除测试对象
	 * 删除之前需要解除与之关联的WebStep
	 * @param objectId
	 */
	public void delObject(Integer objectId){		
		webObjectDao.delete(objectId);
	}
	
	/**
	 * 根据对象名称查找测试对象
	 * 验证对象名称的唯一性
	 * @param objectName
	 * @return
	 */
	public WebObject findObjectByName(String objectName){
		return webObjectDao.findObjectByName(objectName);
	}
	
	/**
	 * 根据ID查找对应的测试对象
	 * @param ObjectId
	 * @return
	 */
	public WebObject findObject(Integer objectId){
		return webObjectDao.get(objectId);
	}
	
}
