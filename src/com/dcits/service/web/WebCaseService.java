package com.dcits.service.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.web.WebCase;
import com.dcits.bean.web.WebStep;
import com.dcits.dao.web.WebCaseDao;
import com.dcits.dao.web.WebStepDao;

@Service
public class WebCaseService {
	
	@Autowired
	private WebCaseDao webCaseDao;
	
	@Autowired
	private WebStepDao webStepDao;
	
	/**
	 * 查询所有的测试用例-仅包括自己创建的
	 * @return
	 */
	public List<WebCase> findAll(Integer userId){
		return webCaseDao.findMyCases(userId);
	}
	
	/**
	 * 增加一条测试用例
	 * 或者编辑更新测试用例
	 * @param c
	 * @return
	 */
	public void editCase(WebCase c){
		webCaseDao.edit(c);
	}
	
	
	/**
	 * 根据ID查找指定测试用例信息
	 * @param caseId
	 * @return
	 */
	public WebCase getCase(Integer caseId){
		return webCaseDao.get(caseId);
	}
	
	/**
	 * 删除测试用例
	 * 删除前需要手动解除关系并删除相关的数据
	 * @param caseId
	 */
	public void delCase(Integer caseId){
		webCaseDao.delete(caseId);
	}
	
	/**
	 * 删除测试步骤
	 * 删除前需要解除与webReport的关联关系
	 * 删除后需要重新计算测试用例下的测试步骤的执行顺序
	 * @param stepId
	 */
	public void delStep(Integer stepId){
		webStepDao.delete(stepId);		
	}
	
	/**
	 * 删除一个测试步骤之后重新对orderNum进行排序
	 * @param caseId
	 */
	public void resortSteps(Integer caseId,Integer orderNum){
		WebCase c=getCase(caseId);	
		List<WebStep> steps=new ArrayList<WebStep>(c.getSteps());
		
		int size=steps.size();
		if(orderNum<=size){
			WebStep step;	
			for(int i=0;i<size-1;i++){
				for(int j=i+1;j<size;j++){
					if(steps.get(i).getOrderNum()>steps.get(j).getOrderNum()){
						step=steps.get(i);
						steps.set(i, steps.get(j));
						steps.set(j, step);
						
					}
				}
			}
			for(int i=0;i<size;i++){
				steps.get(i).setOrderNum(i+1);
			}
			c.addSteps(new HashSet<WebStep>(steps));
			editCase(c);
		}	
	}
	
	/**
	 * 更新或者新增测试步骤
	 * @param step
	 */
	public void editStep(WebStep step){
		webStepDao.edit(step);
	}
	
	/**
	 * 查找指定的webStep
	 * @param stepId
	 * @return
	 */
	public WebStep getStep(Integer stepId){
		return webStepDao.get(stepId);
	}
}
