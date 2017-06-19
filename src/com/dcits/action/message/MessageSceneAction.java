package com.dcits.action.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dcits.bean.message.MessageScene;
import com.dcits.bean.message.TestData;
import com.dcits.service.message.MessageSceneService;
import com.dcits.service.message.MessageService;
import com.dcits.service.message.TestDataService;
import com.dcits.util.GJsonFormatUtil;
import com.dcits.util.JsonUtil;
import com.opensymphony.xwork2.ActionSupport;


@Controller
public class MessageSceneAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private MessageSceneService msService;
	
	@Autowired
	private TestDataService tdService;
	
	@Autowired
	private MessageService mService;
	
	private MessageScene ms;
	
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	private Integer messageId;
	
	private Integer messageSceneId;
	
	private String typeName;
	
	private String typeValue;
	
	private Integer dataId;
	
	private String dataJson;
	
	private String dataDiscr;
	
	private String mark;
	
	private String sceneName;
	
	private String validateRuleFlag;
	
	//获取指定messageId的场景列表
	public String list(){
		List<MessageScene> mss=msService.findByMessageId(messageId);
		for(MessageScene s:mss){
			s.setValidateMethodStr();
		}
		jsonMap.put("data", mss);
		jsonMap.put("returnCode", 0);	
		return SUCCESS;
	}
	
	//保存场景
	public String save(){
		ms=new MessageScene();
		ms.setMessage(mService.getMessageById(messageId));
		ms.setMark(mark);
		ms.setSceneName(sceneName);
		ms.setValidateRuleFlag("0");
		msService.saveMessageScene(ms);
		jsonMap.put("returnCode", 0);	
		return SUCCESS;
	}
	
	//删除指定场景
	public String del(){
			msService.delMessageScene(messageSceneId);
			jsonMap.put("returnCode", 0);		
		return SUCCESS;
	}
	
	//更改信息
	public String edit(){
		msService.editMessageScene(messageSceneId, typeName, typeValue);
		jsonMap.put("returnCode", 0);		
		return SUCCESS;
	}
	
	//获取指定ID的场景信息和数据,针对测试需要的要素加强返回的内容
	public String get(){
		List<String> urls=new ArrayList<String>();
		ms=msService.getMessageScene(messageSceneId);
		ms.setInterfaceName();
		ms.setMessageName();
		
		String iRequestUrlMock=ms.getMessage().getInterfaceInfo().getRequestUrlMock();
		String iRequestUrlReal=ms.getMessage().getInterfaceInfo().getRequestUrlReal();
		String mRequestUrl=ms.getMessage().getRequestUrl();
		
		if(iRequestUrlMock!=""){
			urls.add(iRequestUrlMock);
		}
		
		if(iRequestUrlReal!=""){
			urls.add(iRequestUrlReal);
		}
		
		if(mRequestUrl!=""){
			urls.add(mRequestUrl);
		}
		
		jsonMap.put("urls", urls);
		jsonMap.put("testData",ms.getTestDatas() );
		jsonMap.put("messageScene", ms);
		jsonMap.put("returnCode", 0);			
		
		return SUCCESS;
	}
	
	//删除测试数据
	public String delData(){
		tdService.delData(dataId);
		jsonMap.put("returnCode", 0);					
		return SUCCESS;
	}
	
	//获取指定数据信息
	public String getData(){
		TestData td=tdService.getData(dataId);
		if(td!=null){
			jsonMap.put("returnCode", 0);
			jsonMap.put("dataJson", td.getParamsData());
		}else{
			jsonMap.put("returnCode", 2);
		}					
		return SUCCESS;
	}
	
	
	//更新dataJson
	@SuppressWarnings("unchecked")
	public String updateDataJson(){
		List<String> names=(List<String>) JsonUtil.getJsonList(dataJson,1);
		if(names!=null){
			ms=msService.getMessageScene(messageSceneId);				
			
			List<String> names2=(List<String>) JsonUtil.getJsonList(ms.getMessage().getParameterJson(),1);
			//System.out.println("names2="+names2.toString()+"======names="+names.toString());
			String returnJson=GJsonFormatUtil.formatJsonStr(dataJson);
			if(names.toString().equals(names2.toString())){
				if(dataId!=null){
					tdService.updateDataJson(returnJson, dataId);
					jsonMap.put("status", tdService.getData(dataId).getStatus());
					jsonMap.put("dataJson", returnJson);
				}
				jsonMap.put("returnCode", 0);
			}else{
				jsonMap.put("returnCode", 3);//json入参节点出错,请检查
				jsonMap.put("dataJson", returnJson);
			}
		}else{
			jsonMap.put("returnCode", 2);//json格式不正确
		}
		
		return SUCCESS;
	}
	
	//更新数据状态
	public String updateDataStatus(){
		tdService.updateDataStatus(typeValue, dataId);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//获取报文json入参
	public String getMessageJson(){
		ms=msService.getMessageScene(messageSceneId);
		jsonMap.put("messageJson", ms.getMessage().getParameterJson());
		jsonMap.put("returnCode", 0);				
		return SUCCESS;
	}
	
	
	//保存data数据
	public String saveData(){
			//判断数据标识的重复性
		boolean vp = tdService.findDataByDiscr(dataDiscr, messageSceneId);
		if(vp){
			jsonMap.put("returnCode", 2);//有相同的数据标识
		}else{
			ms=msService.getMessageScene(messageSceneId);
			dataJson=GJsonFormatUtil.formatJsonStr(dataJson);
			TestData td=new TestData(ms,dataJson,"0",dataDiscr);
			Integer id1=tdService.saveData(td);
			jsonMap.put("returnCode", 0);
			jsonMap.put("dataId", id1);
		}			

		
		return SUCCESS;
	}
	
	//更改场景的验证规则
	public String changeValidateRule(){
		msService.changeValidateFlag(messageSceneId, validateRuleFlag);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	

	/*********************************************GET-SET***********************************************************************/
	public void setValidateRuleFlag(String validateRuleFlag) {
		this.validateRuleFlag = validateRuleFlag;
	}
	
	public void setMark(String mark) {
		this.mark = mark;
	}
	
	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}
	
	public void setDataDiscr(String dataDiscr) {
		this.dataDiscr = dataDiscr;
	}
	
	public void setDataJson(String dataJson) {
		this.dataJson = dataJson;
	}
	
/*	public void setmService(MessageService mService) {
		this.mService = mService;
	}
	
	public void setTdService(TestDataService tdService) {
		this.tdService = tdService;
	}*/
	
	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}
	
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
	}
	
	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}
	
	public void setMessageSceneId(Integer messageSceneId) {
		this.messageSceneId = messageSceneId;
	}
	
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}
	
	public void setMsService(MessageSceneService msService) {
		this.msService = msService;
	}
	
}
