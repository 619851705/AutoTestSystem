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
	 * ���ݴ����type����ָ���Ĺ����������
	 * 0-ֻ���ҿ��õ�   1-���Ҳ�Ϊ�Ѵ�ص�
	 * @param type
	 * @return
	 */
	public List<WebStepCategory> findAll(Integer type){
		return cDao.findAll(type);
	}
	
	/**
	 * ����ָ����category
	 * @param categoryId
	 * @return
	 */
	public WebStepCategory getCategory(Integer categoryId){
		return cDao.get(categoryId);
	}
	
	/**
	 * copyָ��category�µ�steps��webCase��
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
	 * �����µĹ������Բ������
	 * @param category
	 * @return
	 */
	public Integer saveCategory(WebStepCategory category){
		return cDao.save(category);
	}
	
	/**
	 * ��ָ����һ�����Բ���copy�ɹ������Բ���
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
	 * ������ʵ�û������ҹ����������
	 * @param realName
	 * @return
	 */
	public List<WebStepCategory> findByUser(String realName){
		return cDao.findByUser(realName);
	}
	
	/**
	 * ����ָ��ID��stepCategory��״̬
	 * @param categoryId
	 * @param status
	 */
	public void updateCategoryStatus(Integer categoryId,String status){
		cDao.updateStatus(categoryId, status);
	}
	
	/**
	 * ��ȡָ����publicStep
	 * @param stepId
	 * @return
	 */
	public WebStepPublic getStep(Integer stepId){
		return dao.get(stepId);
	}
	
	/**
	 * ����ָ����step
	 * @param step
	 */
	public void editStep(WebStepPublic step){
		dao.edit(step);
	}
	
	/**
	 * ɾ��ָ����stepCategory
	 * @param categoryId
	 */
	public void delCategory(Integer categoryId){
		cDao.delete(categoryId);
	}
	
	/**
	 * ����stepCategory��desc��ע
	 * @param categoryId
	 * @param desc
	 */
	public void updateCategoryDesc(Integer categoryId,String desc){
		cDao.updateDesc(categoryId, desc);
	}
}
