package com.dcits.bean.web;

import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

public class WebObjectCategory {
	
	private Integer categoryId;
	private String categoryName;
	private String categoryType;
	
	//所有的子节点集合
	private Set<WebObjectCategory> webObjectCategories=new HashSet<WebObjectCategory>();
	//只有page类型的拥有WebObject集合
	private Set<WebObject> webObjects=new HashSet<WebObject>();
	//自身的父节点,website类型的没有  其为null
	private WebObjectCategory webObjectCategory;
	
	private String name;
	
	private Integer parentCategoryId;
	
	private Integer objectNum;
	
	public Integer getObjectNum() {
		return this.objectNum;
	}

	public void setObjectNum() {
		this.objectNum = this.webObjects.size()+getChildObjectNum(this);
	}

	public Integer getChildObjectNum(WebObjectCategory c){
		Integer a=0;
		for(WebObjectCategory w:c.getWebObjectCategories()){
			a+=w.getWebObjects().size()+getChildObjectNum(w);
		}
		return a;
	}

	public void setName() {
		this.name = this.categoryName;
	}
	
	public void setParentCategoryId() {
		if(webObjectCategory!=null){
			this.parentCategoryId = webObjectCategory.getCategoryId();
		}else{
			this.parentCategoryId=0;
		}
		
	}
	
	public String getName() {
		return name;
	}
	
	public Integer getParentCategoryId() {
		return parentCategoryId;
	}

	@JSON(serialize=false)
	public WebObjectCategory getWebObjectCategory() {
		return webObjectCategory;
	}

	public void setWebObjectCategory(WebObjectCategory webObjectCategory) {
		this.webObjectCategory = webObjectCategory;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	@JSON(serialize=false)
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	@JSON(serialize=false)
	public Set<WebObjectCategory> getWebObjectCategories() {
		return webObjectCategories;
	}

	public void setWebObjectCategories(Set<WebObjectCategory> webObjectCategories) {
		this.webObjectCategories = webObjectCategories;
	}
	@JSON(serialize=false)
	public Set<WebObject> getWebObjects() {
		return webObjects;
	}

	public void setWebObjects(Set<WebObject> webObjects) {
		this.webObjects = webObjects;
	}

	public WebObjectCategory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WebObjectCategory(String categoryName, String categoryType,
			WebObjectCategory webObjectCategory) {
		super();
		this.categoryName = categoryName;
		this.categoryType = categoryType;
		this.webObjectCategory = webObjectCategory;
	}

	
	
	
	
}
