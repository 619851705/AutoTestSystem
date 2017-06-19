package com.dcits.action.message;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dcits.bean.message.InterfaceInfo;
import com.dcits.bean.message.Parameter;
import com.dcits.bean.user.User;
import com.dcits.service.message.InterfaceInfoService;
import com.dcits.service.message.ParameterService;
import com.dcits.util.JsonUtil;
import com.dcits.util.StrutsMaps;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@Controller
public class InterfaceInfoAction extends ActionSupport implements ModelDriven<InterfaceInfo>,Preparable{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private InterfaceInfoService service;
	
	@Autowired
	private ParameterService pService;
	
	private InterfaceInfo interfaceInfo;
	
	private Integer parameterId;
	
	private String attrName; 
	
	private String attrValue;
	//ajax调用返回的map
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	private Integer interfaceId;
	
	private String condition;
	
	/*************************/
	private String parameterIdentify;
	private String parameterName;
	private String defaultValue;
	private String type;	
	/***************************/
	
	private String paramsJson;
	
	//获取接口列表
	public String list(){
		List<InterfaceInfo> interfaces = new ArrayList<InterfaceInfo>();
		interfaces = service.findAllInterfaces();
		jsonMap.put("returnCode", 0);
		jsonMap.put("data", interfaces);
		return SUCCESS;
	}
	
	//删除接口
	public String del(){
		service.delInterface(interfaceId);
		jsonMap.put("returnCode", 0);		
		return SUCCESS;
	}
	
	public void prepareAdd(){
		interfaceInfo=new InterfaceInfo();
	}
	
	//增加接口
	public String add(){
		InterfaceInfo i1 = service.findInterfaceByName(interfaceInfo.getInterfaceName());
		if(i1==null){
			User user=(User)StrutsMaps.getSessionMap().get("user");
			interfaceInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
			interfaceInfo.setUser(user);
			interfaceInfo.setLastModifyUser(user.getRealName());
			interfaceInfo.setStatus("0");
			service.saveInterfaceInfo(interfaceInfo);
			jsonMap.put("returnCode",0);//正常增加
		}else{
			if(i1.getStatus().equals("2")){
				jsonMap.put("returnCode",3);//询问是否恢复被删除的接口
				jsonMap.put("interfaceId",i1.getInterfaceId());
			}else{
				jsonMap.put("returnCode",2);//提示已有此名称的接口
			}
		}			

		
		return SUCCESS;
	}
	
	//启用已删除的接口
	public String recover(){
		service.changeStatus(interfaceId, "0");
		jsonMap.put("returnCode", 0);	
		return SUCCESS;
	}
	
	
	
	//参看接口的入参
	public String showParameters(){
		List<Parameter> ps=new ArrayList<Parameter>();
		ps=pService.list(interfaceId);
		if(ps.size()<1){
			jsonMap.put("returnCode",2);
		}else{
			jsonMap.put("returnCode", 0);
		}
		jsonMap.put("data", ps);
		
		return SUCCESS;
	}

	
	//删除某个接口的一个参数
	public String delParameter(){
		pService.delParameter(parameterId);
		jsonMap.put("returnCode", 0);		
		return SUCCESS;
	}
	
	//编辑接口的参数
	public String editParameter(){
		//System.out.println("attrName="+attrName);
		pService.editParameter(parameterId, attrName, attrValue);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	
	//保存一个新的接口参数
	public String saveParameter(){		
		interfaceInfo = service.getInterfaceInfoById(interfaceId);		
		Parameter p = new Parameter(parameterIdentify.toUpperCase(),parameterName,defaultValue,type,interfaceInfo);
		parameterId = pService.saveParameter(p);
		jsonMap.put("returnCode", 0);
		jsonMap.put("parameterId", parameterId);					
		return SUCCESS;
	}
	
	//通过json导入入参节点
	@SuppressWarnings("unchecked")
	public String batchImportParams(){
		Object a[] = (Object[]) JsonUtil.getJsonList(paramsJson,3);
		if(a!=null){			
			Map<String,String> valueMap=(Map<String, String>)a[2];
			List<String> paramList=(List<String>) a[0];
			List<String> typeList=(List<String>) a[1];
			InterfaceInfo info=service.getInterfaceInfoById(interfaceId);
			for(int i=0;i<paramList.size();i++){
				Parameter param=new Parameter(paramList.get(i),"",valueMap.get(paramList.get(i)),typeList.get(i),info);
				pService.saveParameter(param);
			}
			jsonMap.put("returnCode", 0);
		}else{
			jsonMap.put("returnCode", 2);
		}
		return SUCCESS;
	}
	
	//查询某个接口
	public String find(){
		interfaceInfo = service.getInterfaceInfoById(interfaceId);
		jsonMap.put("interface", interfaceInfo);
		jsonMap.put("returnCode", 0);				
		return SUCCESS;
	}
	
	public void prepareUpdate(){
		interfaceInfo=new InterfaceInfo();
	}
	
	//更新接口
	public String update(){
		//System.out.println("=============="+interfaceInfo.getInterfaceId());
		InterfaceInfo p1 = service.getInterfaceInfoById(interfaceInfo.getInterfaceId());
		p1.setInterfaceName(interfaceInfo.getInterfaceName());
		p1.setInterfaceCnName(interfaceInfo.getInterfaceCnName());
		//p1.setInterfaceType(interfaceInfo.getInterfaceType());
		p1.setStatus(interfaceInfo.getStatus());
		p1.setRequestUrlMock(interfaceInfo.getRequestUrlMock());
		p1.setRequestUrlReal(interfaceInfo.getRequestUrlReal());
		p1.setLastModifyUser(((User)StrutsMaps.getSessionMap().get("user")).getRealName());
		service.updateInterface(p1);
		jsonMap.put("returnCode",0);					
		return SUCCESS;
	}
	
	//根据接口名称或者ID查询
	public String filter(){
		List<InterfaceInfo> infos = service.findInterfaceByCondition(condition);
		if(infos.size()>0){
			jsonMap.put("returnCode", 0);
			jsonMap.put("data", infos);
		}else{
			jsonMap.put("returnCode", 2);
		}		
		return SUCCESS;
	}
	
	/**************************GET/SET***************************************************************/
	public void setParamsJson(String paramsJson) {
		this.paramsJson = paramsJson;
	}
	
	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	public void setParameterIdentify(String parameterIdentify) {
		this.parameterIdentify = parameterIdentify;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	
	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	public void setInterfaceId(Integer interfaceId) {
		this.interfaceId = interfaceId;
	}

	public void setParameterId(Integer parameterId) {
		this.parameterId = parameterId;
	}
	
	@Override
	public void prepare() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	@JSON(serialize=false)
	public InterfaceInfo getModel() {
		// TODO Auto-generated method stub
		return interfaceInfo;
	}

}
