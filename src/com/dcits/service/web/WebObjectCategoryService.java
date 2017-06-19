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
	 * �������нڵ�
	 * @return
	 */
	public List<WebObjectCategory> findAll(){
		return dao.findAll();
	}
	
	/**
	 * ����ID����ָ���Ľڵ���Ϣ
	 * @param categoryId
	 * @return
	 */
	public WebObjectCategory findById(Integer categoryId){
		return dao.get(categoryId);
	}
	
	/**
	 * ɾ���ڵ���Ϣ
	 * @param categoryId
	 */
	public void del(Integer categoryId){
		dao.delete(categoryId);
	}
	
	/**
	 * ����ָ��category������
	 * @param id
	 * @param name
	 */
	public void updateName(Integer id,String name){
		dao.updateCategoryName(id, name);
	}
	
	/**
	 * ���»�������
	 * @param category
	 */
	public void edit(WebObjectCategory category){
		dao.edit(category);
	}
	
	/**
	 * save����,�������ӵĶ���Id
	 * @param category
	 * @return
	 */
	public Integer save(WebObjectCategory category){
		return dao.save(category);
	}
}
