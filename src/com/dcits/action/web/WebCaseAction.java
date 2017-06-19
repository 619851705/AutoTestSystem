package com.dcits.action.web;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;

import com.dcits.bean.system.DataDB;
import com.dcits.bean.user.User;
import com.dcits.bean.web.WebCase;
import com.dcits.bean.web.WebConfig;
import com.dcits.bean.web.WebObject;
import com.dcits.bean.web.WebReport;
import com.dcits.bean.web.WebReportCase;
import com.dcits.bean.web.WebStep;
import com.dcits.service.system.DataDBService;
import com.dcits.service.web.WebCaseService;
import com.dcits.service.web.WebConfigService;
import com.dcits.service.web.WebReportService;
import com.dcits.test.web.TestCore;
import com.dcits.util.StrutsMaps;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class WebCaseAction extends ActionSupport implements ModelDriven<WebCase>{
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private WebCaseService service;
	@Autowired
	private WebReportService rService;
	@Autowired
	private WebConfigService cService;
	@Autowired
	private DataDBService dService;
	
	private Map<String,Object> jsonMap=new HashMap<String,Object>();

	private WebCase webCase=new WebCase();
	
	private Integer caseId;
	
	/////////////////////////////
	private Integer stepId;
	private String stepDesc;
	private String stepMethod;
	private String callMethod;
	private String requireParameter;
	private String requireValue;
	private String requireParameterType;
	private String capture;
	/////////////////////////////
	private Integer objectId;
	
	private String sortStr;
	
	//展示当前用户的所有测试用例
	public String list(){
		User user=(User) StrutsMaps.getSessionMap().get("user");
		List<WebCase> cases=service.findAll(user.getUserId());
		
		for(WebCase c:cases){
			c.setStepNum();
		}
		jsonMap.put("data", cases);
		jsonMap.put("returnCode", 0);
		
		return SUCCESS;
	}
	
	//服务器测试
	public String runTest(){
		User user=(User) StrutsMaps.getSessionMap().get("user");
		WebConfig config=cService.findConfig(user.getUserId());
		if(config==null){
			config=cService.findConfig(0);
			WebConfig config1=new WebConfig(user.getUserId(),config.getElementWaitTime(),config.getResultWaitTime(),config.getIePath(),config.getChromePath(),config.getFirefoxPath(),config.getOperaPath(),config.getWindowSize(),config.getErrorInterruptFlag());
			cService.editConfig(config1);
		}
		webCase=service.getCase(caseId);
		if(webCase.getSteps().size()<1){				
			jsonMap.put("returnCode", 2);
			jsonMap.put("msg", "当前测试用例中没有足够的测试步骤去执行,请检查");
			return SUCCESS;
		}
		WebReportCase reportCase=new WebReportCase(webCase,null,new Timestamp(System.currentTimeMillis()));
		int ret=rService.addReportCase(reportCase);
		reportCase.setReportCaseId(ret);
        
        ActionContext ac = ActionContext.getContext();
	 	ServletContext sc = (ServletContext) ac.get(ServletActionContext.SERVLET_CONTEXT);     
        String rootPath= sc.getRealPath("/");
	 	String dirPath="screenshots/"; 
        Map<String,DataDB> dbs = new HashMap<String,DataDB>();
        for(DataDB db:dService.findAll()){
        	dbs.put(String.valueOf(db.getDbId()), db);
        }	 	
		List<WebReport> reports=TestCore.runTest(webCase, reportCase,(rootPath + dirPath),config,user.getRealName(),dbs);
		for(WebReport report:reports){
			rService.addReport(report);
		}
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//删除
	public String delCase(){
		service.delCase(caseId);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//编辑或者新增测试用例
	public String editCase(){
		User user=(User) StrutsMaps.getSessionMap().get("user");
		if(webCase.getCaseId()==null){
			//新增
			webCase.setCreateTime(new Timestamp(System.currentTimeMillis()));
			webCase.setRunFlag("0");
			webCase.setUser(user);				
		}else{
			//修改
			WebCase c=service.getCase(webCase.getCaseId());
			webCase.setCreateTime(c.getCreateTime());
			webCase.setRunFlag(c.getRunFlag());
			webCase.setUser(c.getUser());				
		}
		service.editCase(webCase);
		jsonMap.put("returnCode", 0);	
		return SUCCESS;
	}
	
	
	//获取一个指定的测试用例
	public String getCase(){
		jsonMap.put("webCase",service.getCase(caseId));
		jsonMap.put("returnCode", 0);						
		return SUCCESS;
	}
	
	//获取指定的测试步骤
	public String getStep(){
		jsonMap.put("webStep", service.getStep(stepId));
		jsonMap.put("returnCode", 0);	
		return SUCCESS;
	}
	
	//修改或者新增测试步骤
	public String editStep(){
		WebStep s1=null;
		WebObject o=null;
		if(objectId!=null){	
			o=new WebObject();
			o.setObjectId(objectId);
		}			
		//判断是新增还是修改
		if(stepId==null){				
			webCase=service.getCase(caseId);
			int orderNum=webCase.getSteps().size()+1;
			//新增
			s1=new WebStep(orderNum,webCase,stepDesc,stepMethod,o,callMethod,requireParameter,requireValue,requireParameterType,capture);
							
		}else{
			//修改
			WebStep s2=service.getStep(stepId);
			s1=new WebStep(s2.getOrderNum(), s2.getWebCase(), stepDesc, stepMethod, o, callMethod,requireParameter, requireValue,requireParameterType, capture);
			s1.setStepId(stepId);			
		}
		service.editStep(s1);
		jsonMap.put("returnCode", 0);
		
		return SUCCESS;
	}
	
	//获取指定测试用例下的测试步骤
	public String listStep(){
		webCase=service.getCase(caseId);
		jsonMap.put("data", webCase.getSteps());
		String acquireStr = "";
		for(WebStep s:webCase.getSteps()){
			if(s.getStepMethod().equals("Acquire")){
				acquireStr += s.getRequireParameter()+",";
			}
		}
		jsonMap.put("acquireStr", acquireStr);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//删除测试步骤
	public String delStep(){
		WebStep step=service.getStep(stepId);
		int orderNum=step.getOrderNum();
		service.delStep(stepId);
		service.resortSteps(caseId,orderNum);			
		jsonMap.put("returnCode", 0);	
		return SUCCESS;
	}
	
	//步骤排序操作
	public String sortSteps(){
		String[] strs=sortStr.split(",");
		int a=0;
		WebStep step=null;
		for(int i=1;i<=strs.length;i++){
			a=Integer.parseInt(strs[i-1]);
			step=service.getStep(a);
			step.setOrderNum(i);
			service.editStep(step);
		}
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	/////////////////////////////////GET-SET////////////////////////////////////////////////
	public void setRequireParameter(String requireParameter) {
		this.requireParameter = requireParameter;
	}
	
	public void setRequireParameterType(String requireParameterType) {
		this.requireParameterType = requireParameterType;
	}
	
	public void setSortStr(String sortStr) {
		this.sortStr = sortStr;
	}
	

	public void setStepDesc(String stepDesc) {
		this.stepDesc = stepDesc;
	}
	
	public void setStepMethod(String stepMethod) {
		this.stepMethod = stepMethod;
	}
	
	public void setCallMethod(String callMethod) {
		this.callMethod = callMethod;
	}
	
	public void setRequireValue(String requireValue) {
		this.requireValue = requireValue;
	}
	
	public void setCapture(String capture) {
		this.capture = capture;
	}
	
	public void setObjectId(Integer objectId) {
		this.objectId = objectId;
	}
	
	public void setStepId(Integer stepId) {
		this.stepId = stepId;
	}
	
	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}
	
	@JSON(serialize=false)
	@Override
	public WebCase getModel() {
		// TODO Auto-generated method stub
		return webCase;
	}

}
