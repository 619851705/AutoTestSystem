package com.dcits.service.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.web.WebCaseSet;
import com.dcits.dao.web.WebCaseSetDao;


@Service
public class WebCaseSetService {
	
	@Autowired
	private WebCaseSetDao dao;
	
	/**
	 * 查找所有的测试用例集信息
	 * @return
	 */
	public List<WebCaseSet> findAll(){
		return dao.findAll();
	}
	
	/**
	 * 根据ID获取测试用例集信息
	 * @param setId
	 * @return
	 */
	public WebCaseSet get(Integer setId){
		return dao.get(setId);
	}
	
	/**
	 * 删除指定的caseSet,会删除关联过的case
	 * @param setId
	 */
	public void del(Integer setId){
		dao.delete(setId);
	}
	
	/**
	 * 新增或者修改测试用例集
	 * @param caseSet
	 */
	public void edit(WebCaseSet caseSet){
		dao.edit(caseSet);
	}
	
	/**
	 * 测试次数+1
	 * @param setId
	 */
	public void addCount(Integer setId){
		dao.addTestCount(setId);
	}
}
