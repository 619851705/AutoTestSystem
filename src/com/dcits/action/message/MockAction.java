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
				//��֤��κϷ���
				if(mock.getIfValidate().equals("0")){
					//��Ҫ����Ĳ����ڵ��б�
					String jsonStr1 = mock.getRequestJson();
					List<String> jsonTreeList1 = (List<String>) JsonUtil.getJsonList(jsonStr1, 1);
					//��ȡHttp��ʽPOST���͹���������
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
				    	jsonMap.put("returnMsg", "�������β��ǺϷ���Json����");
						return SUCCESS;
					}
					
				    //ȱ�ٵĲ����ڵ��б�
				    List<String> missingParams = new ArrayList<String>();
				    
				    //�ȶԽڵ�
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
				    	jsonMap.put("returnMsg", "��β�����Ҫ��:ȱ�����½ڵ�,����:"+missingParams.toString());
				    	return SUCCESS;
				    }
				    
				}				
				jsonMap=Json2Map.json2Map(mock.getResponseJson(), jsonMap);										
				service.updateCallCount(mock.getMockId());
			}else{
				jsonMap.put("returnCode", 3);
				jsonMap.put("returnMsg", "�ýӿ��ѽ�ֹ����");
			}				
		}else{
			jsonMap.put("returnCode", 2);
			jsonMap.put("returnMsg", "û���ҵ��˽ӿڵ�mock��Ϣ");
		}		
		return SUCCESS;
	}
	

	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}
}
