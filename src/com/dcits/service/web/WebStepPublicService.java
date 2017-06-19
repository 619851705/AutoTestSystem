package com.dcits.service.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.web.WebCase;
import com.dcits.bean.web.WebStep;
import com.dcits.bean.web.WebStepCategory;
import com.dcits.bean.web.WebStepPublic;
import com.dcits.dao.web.WebCaseDao;
import com.dcits.dao.web.WebStepCategoryDao;
import com.dcits.dao.web.WebStepDao;
import com.dcits.dao.web.WebStepPublicDao;

@Service
public class WebStepPublicService {
	
	@Autowired
	private WebStepPublicDao dao;
	@Autowired
	private WebStepCategoryDao cDao;
	@Autowired
	private WebStepDao sDao;
	@Autowired
	private WebCaseDao eDao;
	
	/**
	 * 根据传入的type查找指定的公共步骤分类
	 * 0-只查找可用的   1-查找不为已打回的
	 * @param type
	 * @return
	 */
	public List<WebStepCategory> findAll(Integer type){
		return cDao.findAll(type);
	}
	
	/**
	 * 查找指定的category
	 * @param categoryId
	 * @return
	 */
	public WebStepCategory getCategory(Integer categoryId){
		return cDao.get(categoryId);
	}
	
	/**
	 * copy指定category下的steps到webCase下
	 * @param webCase
	 * @param steps
	 */
	public void copySteps(WebCase webCase,List<WebStepPublic> steps){
		webCase=eDao.get(webCase.getCaseId());
		int ret=webCase.getSteps().size();
		for(WebStepPublic w:steps){
			ret++;
			WebStep step=new WebStep(ret,webCase,w.getStepDesc(),w.getStepMethod(),w.getWebObject(),w.getCallMethod(),w.getRequireParameter(),
					w.getRequireValue(),w.getRequireParameterType(),w.getCapture());
			sDao.edit(step);
		}
	}
	
	/**
	 * 保存新的公共测试步骤分类
	 * @param category
	 * @return
	 */
	public Integer saveCategory(WebStepCategory category){
		return cDao.save(category);
	}
	
	/**
	 * 将指定的一批测试步骤copy成公共测试步骤
	 * @param c
	 * @param stepIdStr
	 */
	public void copyPublicSteps(WebStepCategory c,String stepIdStr){
		String[] ids=stepIdStr.split(",");
		int a=1;
		for(String s:ids){
			WebStep step=sDao.get(Integer.parseInt(s));
			WebStepPublic stepP=new WebStepPublic(a,step.getStepDesc(),step.getStepMethod(),
					step.getCallMethod(),step.getRequireParameter(),step.getRequireValue(),step.getRequireParameterType(),step.getCapture(),step.getWebObject(),c);
			dao.save(stepP);
			a++;
		}
	}
	
	/**
	 * 根据真实用户名查找公共步骤分类
	 * @param realName
	 * @return
	 */
	public List<WebStepCategory> findByUser(String realName){
		return cDao.findByUser(realName);
	}
	
	/**
	 * 更新指定ID的stepCategory的状态
	 * @param categoryId
	 * @param status
	 */
	public void updateCategoryStatus(Integer categoryId,String status){
		cDao.updateStatus(categoryId, status);
	}
	
	/**
	 * 获取指定的publicStep
	 * @param stepId
	 * @return
	 */
	public WebStepPublic getStep(Integer stepId){
		return dao.get(stepId);
	}
	
	/**
	 * 更新指定的step
	 * @param step
	 */
	public void editStep(WebStepPublic step){
		dao.edit(step);
	}
	
	/**
	 * 删除指定的stepCategory
	 * @param categoryId
	 */
	public void delCategory(Integer categoryId){
		cDao.delete(categoryId);
	}
	
	/**
	 * 更新stepCategory的desc备注
	 * @param categoryId
	 * @param desc
	 */
	public void updateCategoryDesc(Integer categoryId,String desc){
		cDao.updateDesc(categoryId, desc);
	}
}
