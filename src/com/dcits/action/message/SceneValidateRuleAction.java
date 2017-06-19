package com.dcits.action.message;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dcits.bean.message.MessageScene;
import com.dcits.bean.message.SceneValidateRule;
import com.dcits.service.message.SceneValidateRuleService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Controller
public class SceneValidateRuleAction extends ActionSupport implements ModelDriven<SceneValidateRule>{

	private static final long serialVersionUID = 1L;
	@Autowired
	private SceneValidateRuleService service;
	
	private SceneValidateRule validateRule = new SceneValidateRule();
	
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	private Integer messageSceneId;
	
	//����ָ�������ȡ��֤������Ŀ-ȫ�ֵ�
	public String getValidate(){		
		if(validateRule.getFullValidateFlag().equals("0")){
			SceneValidateRule r = service.getFullValidate(messageSceneId);
			if(r!=null){
				jsonMap.put("validateId", r.getValidateId());
				jsonMap.put("validateValue", r.getValidateValue());
				jsonMap.put("returnCode", 0);
			}else{
				jsonMap.put("returnCode", 2);
			}
		}	
		return SUCCESS;
	}
	
	
	//ȫ����֤����༭ 
	public String fullValidateEdit(){
		if(validateRule.getValidateId()!=null){
			//����
			service.updateValidateValue(validateRule.getValidateId(), validateRule.getValidateValue());
		}else{
			//����
			validateRule.setStatus("0");
			validateRule.setFullValidateFlag("0");
			MessageScene ms = new MessageScene();
			ms.setMessageSceneId(messageSceneId);
			validateRule.setMessageScene(ms);
			service.edit(validateRule);			
		}
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//��ȡ������֤�����б�
	public String listParameterValidate(){
		jsonMap.put("data", service.findParameterValidate(messageSceneId));
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//������֤�����״̬
	public String updateStatus(){
		service.updateStatus(validateRule.getValidateId(), validateRule.getStatus());
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//ɾ������
	public String del(){		
		service.delValidate(validateRule.getValidateId());
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//���������޸�
	public String edit(){
		if(validateRule.getComplexFlag()==null||validateRule.getComplexFlag().equals("")){
			validateRule.setComplexFlag("1");
		}
		service.edit(validateRule);		
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	
	//��ȡָ���Ĺ�����Ϣ-������֤
	public String get(){
		jsonMap.put("validate", service.get(validateRule.getValidateId()));
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	public void setMessageSceneId(Integer messageSceneId) {
		this.messageSceneId = messageSceneId;
	}
	
	
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}
	
	
	@JSON(serialize=false)
	@Override
	public SceneValidateRule getModel() {
		// TODO Auto-generated method stub
		return validateRule;
	}

}
