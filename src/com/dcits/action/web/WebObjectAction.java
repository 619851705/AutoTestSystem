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
	
	////////////////category的移动操作////////////////////
	private Integer targetCategoryId; //移动到的目标categoryId
	private String moveType; //拖放类型 "inner"：成为子节点，"prev"：成为同级前一个节点，"next"：成为同级后一个节点
	
	
	private String categoryType;
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	
	//增加节点
	public String addCategory(){
		WebObjectCategory c1=new WebObjectCategory();
		c1.setCategoryId(targetCategoryId);
		WebObjectCategory c2=new WebObjectCategory(categoryName,categoryType,c1);
		Integer ret=cService.save(c2);
		jsonMap.put("returnCode", 0);
		jsonMap.put("categoryId", ret);			
		
		return SUCCESS;
	}
	
	
	//category的拖放操作
	public String moveCategory(){
		WebObjectCategory category=cService.findById(categoryId);
		WebObjectCategory targetCategory=cService.findById(targetCategoryId);			
		switch (moveType) {
		case "inner":  //成为子节点
			category.setWebObjectCategory(targetCategory);
			break;
		default:  //成为同辈节点
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

	//增加和修改通用一个请求
	public String edit(){					
		if(categoryId==null){
			jsonMap.put("returnCode", 2);
			jsonMap.put("msg", "你还没有选择一个正确的页面,你可以点击左侧的目录树来创建!");
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
	
	//获取所有节点信息
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
	
	//删除节点,删除将会删除下属的所有根结点
	public String delCategory(){		
		cService.del(categoryId);
		jsonMap.put("returnCode", 0);
		
		return SUCCESS;
	}
	
	//更新category名称
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
