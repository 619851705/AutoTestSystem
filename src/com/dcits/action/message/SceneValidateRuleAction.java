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
	
	//根据指定规则获取验证规则条目-全局的
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
	
	
	//全文验证规则编辑 
	public String fullValidateEdit(){
		if(validateRule.getValidateId()!=null){
			//更新
			service.updateValidateValue(validateRule.getValidateId(), validateRule.getValidateValue());
		}else{
			//新增
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
	
	//获取参数验证规则列表
	public String listParameterValidate(){
		jsonMap.put("data", service.findParameterValidate(messageSceneId));
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//更新验证规则的状态
	public String updateStatus(){
		service.updateStatus(validateRule.getValidateId(), validateRule.getStatus());
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//删除操作
	public String del(){		
		service.delValidate(validateRule.getValidateId());
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//新增或者修改
	public String edit(){
		if(validateRule.getComplexFlag()==null||validateRule.getComplexFlag().equals("")){
			validateRule.setComplexFlag("1");
		}
		service.edit(validateRule);		
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	
	//获取指定的规则信息-参数验证
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
