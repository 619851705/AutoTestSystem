package com.dcits.action.message;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dcits.bean.message.InterfaceMock;
import com.dcits.bean.system.GlobalSetting;
import com.dcits.bean.user.User;
import com.dcits.service.message.MockService;
import com.dcits.util.GJsonFormatUtil;
import com.dcits.util.JsonUtil;
import com.dcits.util.StrutsMaps;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@Controller
public class MockInfoAction extends ActionSupport implements ModelDriven<InterfaceMock>,Preparable{
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private MockService service;
	
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	private InterfaceMock mock;
	
	private Integer mockId;
	
	private Integer editFlag;

	
	
	@SuppressWarnings("unchecked")
	public String list(){
		List<InterfaceMock> mocks=service.findAll();
		
		for(InterfaceMock mock:mocks){
			mock.setCreateUserName();
		}
		
		Map<String,GlobalSetting> settingMap = (Map<String, GlobalSetting>) StrutsMaps.getApplicationMap().get("settingMap");
		
		GlobalSetting home = settingMap.get("home");
		
		jsonMap.put("home", home.getSettingValue()==null?home.getDefaultValue():home.getSettingValue());
		
		jsonMap.put("data", mocks);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	
	public String get(){
		mock=service.get(mockId);
		mock.setCreateUserName();
		jsonMap.put("mock", mock);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	public String del(){
		service.delMock(mockId);
		jsonMap.put("returnCode", 0);			
		return SUCCESS;
	}
	
	public void prepareEdit(){
		mock=new InterfaceMock();
	}
	
	
	@SuppressWarnings("unchecked")
	public String edit(){
		InterfaceMock mock1=service.findByName(mock.getInterfaceName());
		if(editFlag==1&&mock1!=null){
			jsonMap.put("returnCode", 2);
			jsonMap.put("msg", "已存在相同接口名称的mock信息");
			return SUCCESS;
		}
		List<String> names1=(List<String>) JsonUtil.getJsonList(mock.getRequestJson(), 1);
		List<String> names2=(List<String>) JsonUtil.getJsonList(mock.getResponseJson(), 1);
		if(names1==null||names2==null){
			jsonMap.put("returnCode", 3);
			jsonMap.put("msg", "不是合法的JSON格式,请检查");
			return SUCCESS;
		}
		if(editFlag==1){//此时为新增,为0时为编辑				
			mock.setCreateTime(new Timestamp(System.currentTimeMillis()));
			mock.setCallCount(0);
			mock.setStatus("0");
			mock.setUser((User)StrutsMaps.getSessionMap().get("user"));
		}else{
			mock1=service.get(mock.getMockId());
			mock.setCreateTime(mock1.getCreateTime());
			mock.setCallCount(mock1.getCallCount());
			mock.setUser(mock1.getUser());
		}			
		mock.setRequestJson(GJsonFormatUtil.formatJsonStr(mock.getRequestJson()));
		mock.setResponseJson(GJsonFormatUtil.formatJsonStr(mock.getResponseJson()));
		service.edit(mock);
		jsonMap.put("returnCode", 0);			
		return SUCCESS;
	}
	///////////////////////////////////GET-SET////////////////////////////////////////////
	public void setEditFlag(Integer editFlag) {
		this.editFlag = editFlag;
	}
	
	public void setMockId(Integer mockId) {
		this.mockId = mockId;
	}
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}
	@Override
	public void prepare() throws Exception {}

	@Override
	@JSON(serialize=false)
	public InterfaceMock getModel() {
		return mock;
	}
	
	public void setMock(InterfaceMock mock) {
		this.mock = mock;
	}
	

}
