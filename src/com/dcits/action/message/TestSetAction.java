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
	
	//�������в��Լ�-���û��߽��õ�
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
	
	
	//ɾ�����Լ�
	public String del(){		
		tsService.delSet(setId);
		jsonMap.put("returnCode", 0);		
		return SUCCESS;
	}
	
	//�����µĲ��Լ�
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
	
	//����ID����
	public String find(){
		testSet=tsService.findById(setId);
		testSet.setUserName();
		testSet.setSceneNum();
		jsonMap.put("set", testSet);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//�༭���Լ���Ϣ
	public String edit(){
		testSet=tsService.findById(setId);
		testSet.setSetName(setName);
		testSet.setStatus(status);
		testSet.setMark(mark);
		tsService.editSet(testSet);
		jsonMap.put("returnCode", 0);		
		return SUCCESS;
	}
	
	//��ȡ���Լ��µĲ��Գ���
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
	
	//�Ӹò��Լ���ɾ��ָ��ID�Ĳ��Գ���-�������
	public String delScene(){
		tsService.removeScene(setId, messageSceneId);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//��ȡҪ��ӵĳ���
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
	
	//���ָ�����������Լ�
	public String addScene(){
		tsService.addScene(messageSceneId, setId);			
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//��ȡ��ǰ�û������Ĳ��Լ�
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
