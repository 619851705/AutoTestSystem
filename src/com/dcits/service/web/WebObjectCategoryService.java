package com.dcits.service.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.web.WebObjectCategory;
import com.dcits.dao.web.WebObjectCategoryDao;

@Service
public class WebObjectCategoryService {
	@Autowired
	private WebObjectCategoryDao dao;
	
	/**
	 * 查找所有节点
	 * @return
	 */
	public List<WebObjectCategory> findAll(){
		return dao.findAll();
	}
	
	/**
	 * 根据ID查找指定的节点信息
	 * @param categoryId
	 * @return
	 */
	public WebObjectCategory findById(Integer categoryId){
		return dao.get(categoryId);
	}
	
	/**
	 * 删除节点信息
	 * @param categoryId
	 */
	public void del(Integer categoryId){
		dao.delete(categoryId);
	}
	
	/**
	 * 更新指定category的名称
	 * @param id
	 * @param name
	 */
	public void updateName(Integer id,String name){
		dao.updateCategoryName(id, name);
	}
	
	/**
	 * 更新或者新增
	 * @param category
	 */
	public void edit(WebObjectCategory category){
		dao.edit(category);
	}
	
	/**
	 * save增加,返回增加的对象Id
	 * @param category
	 * @return
	 */
	public Integer save(WebObjectCategory category){
		return dao.save(category);
	}
}
