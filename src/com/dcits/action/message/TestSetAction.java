package com.dcits.action.message;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dcits.bean.message.MessageScene;
import com.dcits.bean.message.TestSet;
import com.dcits.bean.user.User;
import com.dcits.service.message.TestSetService;
import com.dcits.util.StrutsMaps;
import com.opensymphony.xwork2.ActionSupport;

@Controller
public class TestSetAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private TestSetService tsService;
	
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	private Integer setId;
	
	private TestSet testSet;
	
	private String status;
	
	private String setName;
	
	private String mark;
	
	private Integer messageSceneId;
	
	private Integer getMode;
	
	//查找所有测试集-可用或者禁用的
	public String list(){		
		List<TestSet> ts=tsService.findAll();
		for(TestSet t:ts){
			t.setUserName();
			t.setSceneNum();
		}
		jsonMap.put("data", ts);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	
	//删除测试集
	public String del(){		
		tsService.delSet(setId);
		jsonMap.put("returnCode", 0);		
		return SUCCESS;
	}
	
	//保存新的测试集
	public String save(){
		TestSet t1=tsService.findByName(setName);
		if(t1!=null){
			jsonMap.put("returnCode", 2);
		}else{
			testSet=new TestSet(setName,(User)StrutsMaps.getSessionMap().get("user"),new Timestamp(System.currentTimeMillis()),"0","");
			tsService.saveSet(testSet);
			testSet.setUserName();
			jsonMap.put("set", testSet);
			jsonMap.put("returnCode", 0);
		}				
		return SUCCESS;
	}
	
	//根据ID查找
	public String find(){
		testSet=tsService.findById(setId);
		testSet.setUserName();
		testSet.setSceneNum();
		jsonMap.put("set", testSet);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//编辑测试集信息
	public String edit(){
		testSet=tsService.findById(setId);
		testSet.setSetName(setName);
		testSet.setStatus(status);
		testSet.setMark(mark);
		tsService.editSet(testSet);
		jsonMap.put("returnCode", 0);		
		return SUCCESS;
	}
	
	//获取测试集下的测试场景
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getScenes(){
		Set<MessageScene> ms=null;
		if(getMode!=null&&getMode==1){
			ms=new HashSet<MessageScene>((List)StrutsMaps.getSessionMap().get("validateScenes"));
		}else{
		testSet=tsService.findById(setId);
		ms=testSet.getMs();
		for(MessageScene m:ms){
			m.setInterfaceName();
			m.setMessageName();
		}
		}
		jsonMap.put("data", ms);
		jsonMap.put("returnCode", 0);	
		return SUCCESS;
	}
	
	//从该测试集下删除指定ID的测试场景-解除关联
	public String delScene(){
		tsService.removeScene(setId, messageSceneId);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//获取要添加的场景
	public String getNotScenes(){
		List<MessageScene> mss=tsService.getAllSceneBySetId(setId);
		for(MessageScene ms:mss){
			ms.setInterfaceName();
			ms.setMessageName();
		}
		jsonMap.put("data", mss);
		jsonMap.put("returnCode", 0);
		
		return SUCCESS;
	}
	
	//添加指定场景到测试集
	public String addScene(){
		tsService.addScene(messageSceneId, setId);			
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//获取当前用户创建的测试集
	public String getMy(){
		User user=(User) StrutsMaps.getSessionMap().get("user");	
		List<TestSet> tss=tsService.getMySet(user.getUserId());
		if(tss.size()>0){
			for(TestSet ts:tss){
				ts.setSceneNum();
			}
			jsonMap.put("sets", tss);
			jsonMap.put("returnCode", 0);
		}else{
			jsonMap.put("returnCode", 2);
		}			
		return SUCCESS;
	}
	
	/////////////////////////////////////////////Get---Set///////////////////////////////////////////////////
	
	public void setGetMode(Integer getMode) {
		this.getMode = getMode;
	}
	
	
	public void setMessageSceneId(Integer messageSceneId) {
		this.messageSceneId = messageSceneId;
	}
	
	public void setMark(String mark) {
		this.mark = mark;
	}
	
	public void setSetName(String setName) {
		this.setName = setName;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setTestSet(TestSet testSet) {
		this.testSet = testSet;
	}
	
	public void setSetId(Integer setId) {
		this.setId = setId;
	}
	
/*	public void setTsService(TestSetService tsService) {
		this.tsService = tsService;
	}*/
	
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}

}
