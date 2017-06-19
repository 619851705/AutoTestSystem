package com.dcits.action.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;

import com.dcits.bean.user.User;
import com.dcits.bean.web.WebCase;
import com.dcits.bean.web.WebObject;
import com.dcits.bean.web.WebStepCategory;
import com.dcits.bean.web.WebStepPublic;
import com.dcits.service.web.WebStepPublicService;
import com.dcits.util.StrutsMaps;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class WebStepPublicAction extends ActionSupport implements ModelDriven<WebStepPublic>{
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private WebStepPublicService pService;

	private WebStepPublic step=new WebStepPublic();
	
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	private Integer categoryId;
	
	private Integer objectId;
	
	private Integer caseId;
		
	private String status;
	
	private String categoryDesc;
	private String categoryName;
	private String stepsStr;
	private String categoryTag;

	public String listCategory(){
		List<WebStepCategory> categorys=pService.findAll(0);
		for(WebStepCategory c:categorys){
			c.setStepNum();
		}
		jsonMap.put("data", categorys);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//copy指定的category下的步骤到指定的case
	public String copySteps(){
		List<WebStepPublic> steps=new ArrayList<WebStepPublic>(pService.getCategory(categoryId).getSteps());
		WebCase webCase=new WebCase();
		webCase.setCaseId(caseId);
		pService.copySteps(webCase, steps);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//推荐自己的测试步骤到公共步骤库
	public String addCategory(){
		User user=(User) StrutsMaps.getSessionMap().get("user");
		WebStepCategory stepCategory=new WebStepCategory(categoryName,categoryDesc,
				user.getRealName(),new Timestamp(System.currentTimeMillis()),categoryTag,0,"1");
		int ret=pService.saveCategory(stepCategory);
		stepCategory.setCategoryId(ret);			
		pService.copyPublicSteps(stepCategory,stepsStr);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//展示当前用户的推荐记录
	public String auditRecord(){
		User user=(User) StrutsMaps.getSessionMap().get("user");
		List<WebStepCategory> categorys=pService.findByUser(user.getRealName());
		jsonMap.put("data", categorys);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	
	//更改category状态
	public String updateCategoryStatus(){
		pService.updateCategoryStatus(categoryId, status);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//获取category下的step
	public String listStep(){
		WebStepCategory stepCategory=pService.getCategory(categoryId);
		List<WebStepPublic> steps=new ArrayList<WebStepPublic>(stepCategory.getSteps());
		jsonMap.put("data", steps);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//获取单个publicStep
	public String getStep(){
		step=pService.getStep(step.getStepId());
		jsonMap.put("webStep", step);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
		
	}
	
	//编辑stepPublic
	public String editStep(){
		WebObject o=null;
		if(objectId!=null){	
			o=new WebObject();
			o.setObjectId(objectId);
		}
		WebStepPublic step2=pService.getStep(step.getStepId());
		step.setWebObject(o);
		step.setOrderNum(step2.getOrderNum());
		step.setWebStepCategory(step2.getWebStepCategory());
		pService.editStep(step);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//删除stepCategory
	public String delStepCategory(){
		pService.delCategory(categoryId);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//更新categoryDesc
	public String editCategory(){		
		pService.updateCategoryDesc(categoryId, categoryDesc);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//获取需要审核的stepCategory
	public String categoryAudit(){
		List<WebStepCategory> categories=pService.findAll(2);
		for(WebStepCategory c:categories){
			c.setStepNum();
		}
		jsonMap.put("data", categories);
		jsonMap.put("returnCode", 0);
		
		return SUCCESS;
	}
	/////////////////////////////////////////GET-SET/////////////////////////////////////////////////////
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public void setCategoryTag(String categoryTag) {
		this.categoryTag = categoryTag;
	}
	
	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
	
	public void setObjectId(Integer objectId) {
		this.objectId = objectId;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setStepsStr(String stepsStr) {
		this.stepsStr = stepsStr;
	}
	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	
	
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
		
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}
	
	@JSON(serialize=false)
	@Override
	public WebStepPublic getModel() {
		// TODO Auto-generated method stub
		return step;
	}

}
