package com.dcits.action.message;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dcits.bean.message.InterfaceInfo;
import com.dcits.bean.message.Message;
import com.dcits.bean.message.Parameter;
import com.dcits.bean.user.User;
import com.dcits.service.message.InterfaceInfoService;
import com.dcits.service.message.MessageService;
import com.dcits.util.GJsonFormatUtil;
import com.dcits.util.JsonUtil;
import com.dcits.util.StrutsMaps;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;


@Controller
public class MessageAction extends ActionSupport implements ModelDriven<Message>,Preparable{
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private MessageService mService;
	
	@Autowired
	private InterfaceInfoService iService;
		
	private Message message;
	
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	private Integer messageId;
	
	private String jsonStr;
	
	private Integer interfaceId;
	
	private String messageName;
	
	private String requestUrl;
	
	private String status;
	
	//获取报文列表
	public String list(){
		List<Message> msgs=mService.findAllMessages();
		for(Message m:msgs){
			m.setInterfaceName();
			m.setCreateUserName();
			m.realStatus();
		}
		jsonMap.put("data", msgs);
		jsonMap.put("returnCode", 0);	
		return SUCCESS;
	}
	
	//普通删除
	public String del(){
		mService.delMessage(messageId);
		jsonMap.put("returnCode", 0);		
		return SUCCESS;
	}
	
	//格式化报文的入参json串
	public String format(){
		String returnJson=GJsonFormatUtil.formatJsonStr(jsonStr);
		if(returnJson!=null){
			jsonMap.put("returnCode", 0);
			jsonMap.put("returnJson", returnJson);
		}else{
			jsonMap.put("returnCode", 2);
		}		
		return SUCCESS;
	}
	
	//验证入参的正确性
	@SuppressWarnings("unchecked")
	public String validateJson(){
		Set<Parameter> ps=(iService.getInterfaceInfoById(interfaceId)).getParameters();					
			List<String> names=(List<String>) JsonUtil.getJsonList(jsonStr,1);
			if(names!=null){
			int flag=1;
			int aFlag=0;
			String msg="入参节点:";
			for(String name:names){
				for(Parameter p:ps){
					if(p.getParameterIdentify().toUpperCase().equals(name.toUpperCase())){
						flag=0;
					}
				}
				if(flag==1){
					aFlag=1;
					msg+="["+name+"] ";
				}else{
					flag=1;
				}
			}
			if(aFlag==0){
				jsonMap.put("returnCode", 0);//验证通过
			}else{
				msg+="在接口参数列表中未定义,请检查!";
				jsonMap.put("returnCode", 3);//验证不通过
				jsonMap.put("msg",msg );//验证不通过
			}
			
		}else{
			jsonMap.put("returnCode", 2);//json格式不正确
		}			
		
		return SUCCESS;
	}
	
	
	
	//保存新的报文
	public String add(){
		//List<String> names=(List<String>) JsonUtil.getJsonList(jsonStr,1);
		message=new Message();
		User user=(User)(StrutsMaps.getSessionMap().get("user"));
		message.setMessageName(messageName);
		message.setInterfaceInfo(iService.getInterfaceInfoById(interfaceId));
		message.setRequestUrl(requestUrl);
		message.setUser(user);
		message.setLastModifyUser(user.getRealName());
		message.setCreateTime(new Timestamp(System.currentTimeMillis()));
		message.setStatus("0");
		message.setParameterJson(GJsonFormatUtil.formatJsonStr(jsonStr));
		mService.saveMessage(message);
		jsonMap.put("returnCode", 0);		
		return SUCCESS;
	}
	
	
	//获取报文入参JSON串
	public String getParamsJson(){
		jsonStr=mService.getMessageById(messageId).getParameterJson();
		jsonMap.put("jsonStr", jsonStr);
		jsonMap.put("returnCode", 0);
		
		return SUCCESS;
	}
	
	//获取指定的报文
	public String get(){

		message=mService.getMessageById(messageId);
		jsonMap.put("message", message);
		jsonMap.put("createUser", message.getUser().getRealName());
		InterfaceInfo i = message.getInterfaceInfo();
		jsonMap.put("interfaceId", i.getInterfaceId());
		jsonMap.put("interfaceName", i.getInterfaceName());
		jsonMap.put("returnCode", 0);	
		return SUCCESS;
	}
	
	
	
	//更新报文
	public String edit(){
		message=mService.getMessageById(messageId);
		message.setMessageName(messageName);
		message.setInterfaceInfo(iService.getInterfaceInfoById(interfaceId));
		message.setParameterJson(jsonStr);
		message.setRequestUrl(requestUrl);
		message.setStatus(status);
		message.setLastModifyUser(((User)StrutsMaps.getSessionMap().get("user")).getRealName());
		mService.editMessage(message);			
		jsonMap.put("returnCode", 0);
		
		return SUCCESS;
	}
	
	////////////////////////////////////GET-SET/////////////////////////////////////////////////////
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	
	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}
	
	public void setInterfaceId(Integer interfaceId) {
		this.interfaceId = interfaceId;
	}
	
	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}
	
	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}
	
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	@Override
	public void prepare() throws Exception {}
	
	@JSON(serialize=false)
	@Override
	public Message getModel() {
		return message;
	}
	
}
