package com.dcits.service.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.web.WebCaseSetComp;
import com.dcits.dao.web.WebCaseSetCompDao;

@Service
public class WebCaseSetCompService {
	
	@Autowired
	private WebCaseSetCompDao dao;
	
	/**
	 * 根据caseId和setId查找指定的关联关系
	 * @param caseId
	 * @param setId
	 * @return
	 */
	public WebCaseSetComp get(Integer caseId,Integer setId){
		return dao.get(caseId, setId);
	}
	
	/**
	 * 添加或者修改一条关联关系
	 * @param comp
	 */
	public void edit(WebCaseSetComp comp){
		dao.edit(comp);
	}
	
	/**
	 * 查找指定用户的审核记录
	 * @param userId
	 * @return
	 */
	public List<WebCaseSetComp> findByUser(Integer userId){
		return dao.findByUserId(userId);
	}
	
	/**
	 * 更新状态
	 * @param id
	 * @param status
	 */
	public void updateStatus(Integer id,String status){
		dao.updateStatus(id, status);
	}
	
	/**
	 * 查找正常关联关系
	 * 状态为0或者1    已审核通过和待审核,不显示已打回和已删除
	 * @return
	 */
	public List<WebCaseSetComp> findAll(Integer setId){
		return dao.findAll(setId);
	}
}
