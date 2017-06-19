package com.dcits.action.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dcits.bean.message.InterfaceMock;
import com.dcits.service.message.MockService;
import com.dcits.util.Json2Map;
import com.dcits.util.JsonUtil;
import com.dcits.util.StrutsMaps;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@Controller
public class MockAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private MockService service;
	
	private InterfaceMock mock;
	
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	

	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws IOException{
		String actionName=StrutsMaps.getActionName();
		mock=service.findByName(actionName);
		if(mock!=null){
			if(mock.getStatus().equals("0")){
				//验证入参合法性
				if(mock.getIfValidate().equals("0")){
					//需要满足的参数节点列表
					String jsonStr1 = mock.getRequestJson();
					List<String> jsonTreeList1 = (List<String>) JsonUtil.getJsonList(jsonStr1, 1);
					//获取Http方式POST发送过来的数据
					ActionContext ctx = ActionContext.getContext();
					HttpServletRequest request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST);   

					InputStream inputStream;

					inputStream = request.getInputStream();
					String strMessage = "";
					String strResponse = "";
					BufferedReader reader;
					reader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
					while ((strMessage = reader.readLine()) != null) {
					strResponse += strMessage;
					}
					reader.close();
					inputStream.close();

					strResponse = strResponse.trim();
				    
					List<String> jsonTreeList2 = (List<String>) JsonUtil.getJsonList(strResponse, 1);
					
					if(jsonTreeList2==null){
						jsonMap.put("returnCode", 4);
				    	jsonMap.put("returnMsg", "传入的入参不是合法的Json数据");
						return SUCCESS;
					}
					
				    //缺少的参数节点列表
				    List<String> missingParams = new ArrayList<String>();
				    
				    //比对节点
				    for(String s1:jsonTreeList1){
				    	int flag = 1;
				    	for(String s2:jsonTreeList2){
				    		if(s1.equals(s2)){
				    			flag = 0;
				    		}
				    	}
				    	if(flag == 1){
				    		missingParams.add(s1);
				    	}
				    }
				    if(missingParams.size()!=0){
				    	jsonMap.put("returnCode", 4);
				    	jsonMap.put("returnMsg", "入参不符合要求:缺少以下节点,请检查:"+missingParams.toString());
				    	return SUCCESS;
				    }
				    
				}				
				jsonMap=Json2Map.json2Map(mock.getResponseJson(), jsonMap);										
				service.updateCallCount(mock.getMockId());
			}else{
				jsonMap.put("returnCode", 3);
				jsonMap.put("returnMsg", "该接口已禁止调用");
			}				
		}else{
			jsonMap.put("returnCode", 2);
			jsonMap.put("returnMsg", "没有找到此接口的mock信息");
		}		
		return SUCCESS;
	}
	

	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}
}
