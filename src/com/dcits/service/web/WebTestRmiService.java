package com.dcits.service.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.web.WebTestRmi;
import com.dcits.dao.web.WebTestRmiDao;

@Service
public class WebTestRmiService {
	
	@Autowired
	private WebTestRmiDao dao;
	
	/**
	 * 更新
	 * @param test
	 */
	public void edit(WebTestRmi test){
		dao.edit(test);
	}
	
	/**
	 * 增加保存
	 * @param test
	 * @return
	 */
	public Integer save(WebTestRmi test){
		return dao.save(test);
	}
	
	/**
	 * 获取指定的
	 * @param testId
	 * @return
	 */
	public WebTestRmi get(Integer testId){
		return dao.get(testId);
	}
	
	/**
	 * 根据用户ID查找该用户今日提交的还未开始测试的测试任务
	 * 得到的是集合
	 * @param userId
	 * @return
	 */
	public List<WebTestRmi> getByUser(Integer userId){
		return dao.getByUser(userId);
	}
	
	/**
	 * 获取指定用户的当前测试任务
	 * @param userId
	 * @return
	 */
	public List<WebTestRmi> getUserTask(Integer userId){
		return dao.getUserTask(userId);
	}
}
