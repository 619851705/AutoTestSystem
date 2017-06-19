package com.dcits.action.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dcits.bean.web.WebObject;
import com.dcits.bean.web.WebObjectCategory;
import com.dcits.service.web.WebObjectCategoryService;
import com.dcits.service.web.WebObjectService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Controller
public class WebObjectAction extends ActionSupport implements ModelDriven<WebObject>{
	private static final long serialVersionUID = 1L;

	@Autowired
	private WebObjectService service;
	@Autowired
	private WebObjectCategoryService cService;
	
	private WebObject webObject=new WebObject();
	
	private Integer objectId;
	
	private Integer categoryId;
	
	private String categoryName;
	
	////////////////category���ƶ�����////////////////////
	private Integer targetCategoryId; //�ƶ�����Ŀ��categoryId
	private String moveType; //�Ϸ����� "inner"����Ϊ�ӽڵ㣬"prev"����Ϊͬ��ǰһ���ڵ㣬"next"����Ϊͬ����һ���ڵ�
	
	
	private String categoryType;
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	
	//���ӽڵ�
	public String addCategory(){
		WebObjectCategory c1=new WebObjectCategory();
		c1.setCategoryId(targetCategoryId);
		WebObjectCategory c2=new WebObjectCategory(categoryName,categoryType,c1);
		Integer ret=cService.save(c2);
		jsonMap.put("returnCode", 0);
		jsonMap.put("categoryId", ret);			
		
		return SUCCESS;
	}
	
	
	//category���ϷŲ���
	public String moveCategory(){
		WebObjectCategory category=cService.findById(categoryId);
		WebObjectCategory targetCategory=cService.findById(targetCategoryId);			
		switch (moveType) {
		case "inner":  //��Ϊ�ӽڵ�
			category.setWebObjectCategory(targetCategory);
			break;
		default:  //��Ϊͬ���ڵ�
			WebObjectCategory parentCategory=cService.findById(targetCategory.getWebObjectCategory().getCategoryId());
			category.setWebObjectCategory(parentCategory);
			break;
		}
		cService.edit(category);
		jsonMap.put("returnCode", 0);				
		return SUCCESS;
	}
	
	
	public String list(){
		List<WebObject> os=null;
		if(categoryId!=null){
			os=new ArrayList<WebObject>(cService.findById(categoryId).getWebObjects());
		}else{
			os=service.findAll();
		}			
		jsonMap.put("returnCode", 0);
		jsonMap.put("data", os);
		return SUCCESS;
	}

	//���Ӻ��޸�ͨ��һ������
	public String edit(){					
		if(categoryId==null){
			jsonMap.put("returnCode", 2);
			jsonMap.put("msg", "�㻹û��ѡ��һ����ȷ��ҳ��,����Ե������Ŀ¼��������!");
			return SUCCESS;
		}
		WebObjectCategory category=new WebObjectCategory();
		category.setCategoryId(categoryId);
		webObject.setWebObjectCategory(category);	
		service.editObject(webObject);
		jsonMap.put("returnCode", 0);
		jsonMap.put("msg", "");	
		return SUCCESS;
	}
	
	public String get(){
		webObject=service.findObject(objectId);
		jsonMap.put("webObject", webObject);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	public String del(){
		service.delObject(objectId);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//��ȡ���нڵ���Ϣ
	public String getNodes(){
		List<WebObjectCategory> categories =cService.findAll();
		for(WebObjectCategory w:categories){
			w.setName();
			w.setParentCategoryId();
			w.setObjectNum();
		}
		jsonMap.put("categorys",categories );
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//ɾ���ڵ�,ɾ������ɾ�����������и����
	public String delCategory(){		
		cService.del(categoryId);
		jsonMap.put("returnCode", 0);
		
		return SUCCESS;
	}
	
	//����category����
	public String updateCategoryName(){
		cService.updateName(categoryId, categoryName);			
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	/////////////////////////////////////////GET-SET//////////////////////////////////////////////////////////
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	
	public void setTargetCategoryId(Integer targetCategoryId) {
		this.targetCategoryId = targetCategoryId;
	}
	
	public void setMoveType(String moveType) {
		this.moveType = moveType;
	}
	
	
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}
	
	public void setObjectId(Integer objectId) {
		this.objectId = objectId;
	}
	
	@JSON(serialize=false)
	@Override
	public WebObject getModel() {
		// TODO Auto-generated method stub
		return webObject;
	}
}
