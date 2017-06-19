package com.dcits.action.message;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dcits.bean.message.MessageScene;
import com.dcits.bean.message.SceneValidateRule;
import com.dcits.bean.message.TestConfig;
import com.dcits.bean.message.TestData;
import com.dcits.bean.message.TestReport;
import com.dcits.bean.message.TestResult;
import com.dcits.bean.message.TestSet;
import com.dcits.bean.system.DataDB;
import com.dcits.bean.user.User;
import com.dcits.service.message.AutoTestService;
import com.dcits.service.message.MessageSceneService;
import com.dcits.service.message.SceneValidateRuleService;
import com.dcits.service.message.TestDataService;
import com.dcits.service.message.TestSetService;
import com.dcits.service.system.DataDBService;
import com.dcits.service.user.UserMailService;
import com.dcits.test.message.AutoTest;
import com.dcits.util.GJsonFormatUtil;
import com.dcits.util.StrutsMaps;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@Controller
public class AutoTestAction extends ActionSupport implements ModelDriven<TestConfig>,Preparable{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private AutoTestService atService;
	@Autowired
	private TestDataService tdService;
	@Autowired
	private TestSetService tsService;
	@Autowired
	private MessageSceneService msService;
	@Autowired
	private SceneValidateRuleService ruleService;
	@Autowired
	private DataDBService dbService;
	@Autowired
	private UserMailService mailService;
	
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	private Integer messageSceneId;
	
	private String requestUrl;
	
	private String requestMessage;
	
	private Integer dataId;
	
	private TestResult result;
	
	private TestConfig config;
	
	private Integer setId;
	
	private Integer reportId;
	
	private Integer finishFlag;
	
	
	//验证身份
	public String validatePower(){
		User user=(User) StrutsMaps.getSessionMap().get("user");
		jsonMap.put("returnCode", 0);
		jsonMap.put("role",user.getRole().getRoleName());
		return SUCCESS;
	}
	
	//测试单个测试场景
	public String sceneTest(){
		try {
			config=(TestConfig) StrutsMaps.getSessionMap().get("config");
			TestData d=tdService.getData(dataId);
			if(d.getStatus().equals("1")){
				jsonMap.put("returnCode", 2);
				jsonMap.put("msg", "该条数据已被使用了,请更换其它测试数据!");
				return SUCCESS;
			}
			MessageScene scene = msService.getMessageScene(messageSceneId);
			Integer returnCode=testSceneByF(scene,requestUrl, requestMessage, messageSceneId, dataId, null,config);
			if(returnCode!=null){
				jsonMap.put("result", result);
				jsonMap.put("returnCode", 0);
			}else{
				jsonMap.put("returnCode", 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
					
		return SUCCESS;
	}
	
	//获取指定用户的测试配置
	public String getConfig(){
		/*User user=(User)StrutsMaps.getSessionMap().get("user");*/
		TestConfig config=(TestConfig) StrutsMaps.getSessionMap().get("config");
		/*if(config==null){
			config=atService.getConfigByUserId(0);
		}
		StrutsMaps.getSessionMap().put("config", config);*/
		jsonMap.put("config", config);
		jsonMap.put("returnCode", 0);		
		return SUCCESS;
	}
	
	public void prepareUpdateConfig(){
		config=new TestConfig();
	}
	
	//更新配置信息
	public String updateConfig(){
		TestConfig c=(TestConfig) StrutsMaps.getSessionMap().get("config");
		config.setConfigId(c.getConfigId());				
		config.setUserId(c.getUserId());
		atService.updateTestConfig(config);
		StrutsMaps.getSessionMap().put("config", config);
		jsonMap.put("config", config);
		jsonMap.put("returnCode", 0);		
		return SUCCESS;
	}
	
	//测试集检查数据
	public String validateData(){
		List<MessageScene> mss=null;
		if(setId!=0){
			TestSet ts=tsService.findById(setId);
			mss=new ArrayList<MessageScene>(ts.getMs());
		}else{
			mss=msService.findAll();
		}
		List<MessageScene> nmss=validateSceneData(mss);
		if(nmss.size()>0){				
			for(MessageScene ms:nmss){
				ms.setInterfaceName();
				ms.setMessageName();
			}
			StrutsMaps.getSessionMap().put("validateScenes", nmss);
			jsonMap.put("size", nmss.size());
			jsonMap.put("returnCode", 2);
			//jsonMap.put("data", nmss);
			return SUCCESS;
		}			
		jsonMap.put("returnCode", 0);			
		return SUCCESS;
	}
	
	//创建测试报告
	public String createReport(){
		int ret=0;
		TestReport report=null;
		List<MessageScene> mss=null;
		User user=(User) StrutsMaps.getSessionMap().get("user");
		if(setId!=0){
			TestSet ts=tsService.findById(setId);
			mss=new ArrayList<MessageScene>(ts.getMs());
		}else{
			mss=msService.findAll();				
		}
		for(MessageScene ms:mss){
			ms.setInterfaceName();
			ms.setMessageName();
		}
		report=new TestReport(String.valueOf(setId),"N",new Timestamp(System.currentTimeMillis()),user);
		ret=atService.addReport(report);
		jsonMap.put("scenes",mss);
		jsonMap.put("reportId", ret);
		jsonMap.put("returnCode", 0);				
		return SUCCESS;
	}
	
	//后台执行测试
	public String backgroundTest(){
		config=(TestConfig) StrutsMaps.getSessionMap().get("config");
		TestReport report=atService.findReport(reportId);
		List<MessageScene> mss=null;
		if(setId==0){
			mss=msService.findAll();
		}else{
			TestSet ts=tsService.findById(setId);
			mss=new ArrayList<MessageScene>(ts.getMs());
		}
		List<MessageScene> delMss=validateSceneData(mss);
		String requestUrl="";
		for(MessageScene ms:mss){				
			switch (config.getRequestUrlFlag()) {
			case "0":
				requestUrl=ms.getMessage().getRequestUrl();
				if(requestUrl.equals("")||requestUrl==null){
					requestUrl=ms.getMessage().getInterfaceInfo().getRequestUrlMock();
				}
				break;
			case "1":
				requestUrl=ms.getMessage().getInterfaceInfo().getRequestUrlMock();
				break;
			case "2":
				requestUrl=ms.getMessage().getInterfaceInfo().getRequestUrlReal();
				break;
			}
							
			if(!delMss.contains(ms)){
				Set<TestData> tds=ms.getTestDatas();
				TestData td=tds.toArray(new TestData[tds.size()])[0];
				testSceneByF(ms,requestUrl,td.getParamsData(),ms.getMessageSceneId(),td.getDataId(),report,config);
			}else{
				testSceneByF(ms,requestUrl,"",ms.getMessageSceneId(),null,report,config);
			}
		}
		report.setFinishTime(new Timestamp(System.currentTimeMillis()));
		report.setFinishFlag("Y");
		atService.updateReport(report);	
		return SUCCESS;
	}
	
	//非后台批量测试
	public String commonTest(){	
			TestReport report=atService.findReport(reportId);
			if(finishFlag!=null){
				report.setFinishTime(new Timestamp(System.currentTimeMillis()));
				report.setFinishFlag("Y");
				atService.updateReport(report);
				jsonMap.put("returnCode", 0);
				return SUCCESS;
			}
			config=(TestConfig) StrutsMaps.getSessionMap().get("config");
			MessageScene ms=msService.getMessageScene(messageSceneId);
			int ret=0;
			String requestUrl="";
			switch (config.getRequestUrlFlag()) {
			case "0":
				requestUrl=ms.getMessage().getRequestUrl();
				if(requestUrl.equals("")||requestUrl==null){
					requestUrl=ms.getMessage().getInterfaceInfo().getRequestUrlMock();
				}
				break;
			case "1":
				requestUrl=ms.getMessage().getInterfaceInfo().getRequestUrlMock();
				break;
			case "2":
				requestUrl=ms.getMessage().getInterfaceInfo().getRequestUrlReal();
				break;
			}
			Set<TestData> tds=ms.getTestDatas();
			if(tds.size()<1){
				ret=testSceneByF(ms,requestUrl,"",ms.getMessageSceneId(),null,report,config);
			}else{
				TestData td=tds.toArray(new TestData[tds.size()])[0];
				ret=testSceneByF(ms,requestUrl,td.getParamsData(),ms.getMessageSceneId(),td.getDataId(),report,config);
			}
			
			jsonMap.put("returnCode", 0);
			jsonMap.put("status", ret);					
		return SUCCESS;
	}
	
	//定时任务执行
	public String runTest(){
		TestReport report=null;
		List<MessageScene> mss=null;
		if(setId!=0){
			TestSet ts=tsService.findById(setId);
			mss=new ArrayList<MessageScene>(ts.getMs());
		}else{
			mss=msService.findAll();				
		}
		for(MessageScene ms:mss){
			ms.setInterfaceName();
			ms.setMessageName();
		}
		User user = new User();
		user.setUserId(2);
		report=new TestReport(String.valueOf(setId),"N",new Timestamp(System.currentTimeMillis()),user);
		int ret=atService.addReport(report);
		report.setReportId(ret);
		
		config=atService.getConfigByUserId(0);
		if(setId==0){
			mss=msService.findAll();
		}else{
			TestSet ts=tsService.findById(setId);
			mss=new ArrayList<MessageScene>(ts.getMs());
		}
		List<MessageScene> delMss=validateSceneData(mss);
		String requestUrl="";
		for(MessageScene ms:mss){				
			switch (config.getRequestUrlFlag()) {
			case "0":
				requestUrl=ms.getMessage().getRequestUrl();
				if(requestUrl.equals("")||requestUrl==null){
					requestUrl=ms.getMessage().getInterfaceInfo().getRequestUrlMock();
				}
				break;
			case "1":
				requestUrl=ms.getMessage().getInterfaceInfo().getRequestUrlMock();
				break;
			case "2":
				requestUrl=ms.getMessage().getInterfaceInfo().getRequestUrlReal();
				break;
			}
							
			if(!delMss.contains(ms)){
				Set<TestData> tds=ms.getTestDatas();
				TestData td=tds.toArray(new TestData[tds.size()])[0];
				testSceneByF(ms,requestUrl,td.getParamsData(),ms.getMessageSceneId(),td.getDataId(),report,config);
			}else{
				testSceneByF(ms,requestUrl,"",ms.getMessageSceneId(),null,report,config);
			}
		}
		report.setFinishTime(new Timestamp(System.currentTimeMillis()));
		report.setFinishFlag("Y");
		atService.updateReport(report);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////		
	//检查测试场景的数据量是否足够
	public List<MessageScene> validateSceneData(List<MessageScene> mss){
		List<MessageScene> delMss = new ArrayList<MessageScene>();
		for(MessageScene ms:mss){
				if(ms.getTestDatas().size()<1){
					delMss.add(ms);
				}								
		}
		return delMss;
		
	}
	
	
	
	
	//单个测试场景的测试过程方法,返回测试结果标识
	public Integer testSceneByF(MessageScene scene,String requestUrl,String requestMessage,Integer messageSceneId,Integer dataId,TestReport report,TestConfig config){
		String messageInfo=scene.getMessage().getInterfaceInfo().getInterfaceName()+","+scene.getMessage().getMessageName()+","+scene.getSceneName();
		if(dataId==null&&requestMessage==""){
			//没有测试数据
			result=new TestResult(scene,messageInfo,requestUrl,"没有足够的测试数据,请检查","没有足够的测试数据,请检查",0,"2","0",new Timestamp(System.currentTimeMillis()),"");
			result.setTestReport(report);
			atService.addResult(result);
			return 2;
		}else{
			Map<String,String> map=AutoTest.callService(requestUrl, requestMessage,config);
			//验证方法之后的返回结果
			
			//有返回
			if(map.get("runStatus").equals("0")){
				/*int successFlag=AutoTest.CheckReturn(map.get("responseJson"),config.getValidateString());
				map.put("runStatus", String.valueOf(successFlag));*/
				//验证结果,根据方式给予不同参数
				Map<String,String> returnMap = new HashMap<String,String>();
				switch (scene.getValidateRuleFlag()) {
				case "0":
					returnMap = AutoTest.CheckReturnDefault(map.get("responseJson"),config.getValidateString());
					break;
				case "1":
					//验证参数列表
					List<SceneValidateRule> rules = ruleService.findParameterValidate(messageSceneId);
					//数据库列表
					Map<String,DataDB> dbs = new HashMap<String,DataDB>();
			        for(DataDB db:dbService.findAll()){
			        	dbs.put(String.valueOf(db.getDbId()),db);
			        }
					returnMap = AutoTest.checkReturnParams(map.get("responseJson"), requestMessage, rules, dbs);
					break;
				case "2":
					String validateStr = "";
					SceneValidateRule rule = ruleService.getFullValidate(messageSceneId);
					if(rule!=null){
						validateStr = rule.getValidateValue();
					}
					returnMap = AutoTest.checkReturnFull(map.get("responseJson"),validateStr);
					break;
				}
				map.put("runStatus", returnMap.get("status"));
				map.put("mark", returnMap.get("msg"));
			}
			try {
				TestData td=tdService.getData(dataId);
				if(td.getMessageScene().getMessage().getInterfaceInfo().getInterfaceType().equals("SL")){
					tdService.updateDataStatus("1", dataId);
				}
				String returnJson=GJsonFormatUtil.formatJsonStr(map.get("responseJson"));
				if(returnJson!=null){
					map.put("responseJson", returnJson);
				}
				
				result=new TestResult(td.getMessageScene(),messageInfo,requestUrl,requestMessage,map.get("responseJson"),
						Integer.valueOf(map.get("useTime")),map.get("runStatus"),map.get("statusCode"),new Timestamp(System.currentTimeMillis()),map.get("mark"));
				result.setTestReport(report);
				atService.addResult(result);
				return Integer.valueOf(map.get("runStatus"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
				
		return null;
	}
	
	//////////////////////////////////////GET-SET////////////////////////////////////////////////////
	public void setFinishFlag(Integer finishFlag) {
		this.finishFlag = finishFlag;
	}
	
	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}
	
	public void setSetId(Integer setId) {
		this.setId = setId;
	}

	
	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}
	
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	
	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}
	
	public void setMessageSceneId(Integer messageSceneId) {
		this.messageSceneId = messageSceneId;
	}
	
	
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}

	@Override
	public void prepare() throws Exception {
		
	}

	@Override
	public TestConfig getModel() {
		// TODO Auto-generated method stub
		return config;
	}

}
