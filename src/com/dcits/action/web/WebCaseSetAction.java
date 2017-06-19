package com.dcits.action.web;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dcits.bean.system.DataDB;
import com.dcits.bean.user.User;
import com.dcits.bean.web.WebCase;
import com.dcits.bean.web.WebCaseSet;
import com.dcits.bean.web.WebCaseSetComp;
import com.dcits.bean.web.WebConfig;
import com.dcits.bean.web.WebReport;
import com.dcits.bean.web.WebReportCase;
import com.dcits.bean.web.WebReportSet;
import com.dcits.service.system.DataDBService;
import com.dcits.service.web.WebCaseSetCompService;
import com.dcits.service.web.WebCaseSetService;
import com.dcits.service.web.WebConfigService;
import com.dcits.service.web.WebReportService;
import com.dcits.test.web.TestCore;
import com.dcits.util.StrutsMaps;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Controller
public class WebCaseSetAction extends ActionSupport implements ModelDriven<WebCaseSet>{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private WebCaseSetService service;
	@Autowired
	private WebCaseSetCompService cService;
	@Autowired
	private WebConfigService gService;
	@Autowired
	private WebReportService rService;
	@Autowired
	private DataDBService dService;
	
	private WebCaseSet caseSet=new WebCaseSet();
	
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	private Integer setId;
	
	private Integer id;
	
	private Integer caseId;
	
	private String status;
	
	//获取所有当前的测试用例集
	public String list(){
		List<WebCaseSet> sets=service.findAll();
		for(WebCaseSet w:sets){
			w.setCreateUser();
		}
		jsonMap.put("data", sets);
		jsonMap.put("returnCode", 0);
		
		return SUCCESS;
	}
	
	//添加测试用例到测试用例集
	public String addToSet(){
		WebCaseSetComp comp=cService.get(caseId, setId);
		//没有关联记录
		if(comp==null){
			WebCase webCase=new WebCase();
			webCase.setCaseId(caseId);
			caseSet.setSetId(setId);
			comp=new WebCaseSetComp(caseSet,webCase,"1",(User) StrutsMaps.getSessionMap().get("user"),new Timestamp(System.currentTimeMillis()));
			cService.edit(comp);
			jsonMap.put("returnCode", 0);
			return SUCCESS;				
		}
		//已有关联记录,判断关联状态
		String msg="";
		switch (comp.getStatus()) {
		case "0":
			msg="该测试用例集已有该条测试用例!";
			break;
		case "1":
			msg="你已提交过该条记录,请等待管理员审批!";
			break;
		case "2":
			msg="你上次的提交请求被管理员打回,请至用例管理页面重新提交!";
			break;
		case "3":
			msg="该条测试用例已被管理员从该测试用例中删除,请至用例管理页面重新提交!";
			break;
		}
		jsonMap.put("returnCode", 2);
		jsonMap.put("msg", msg);					
		return SUCCESS;
	}
	
	//删除测试用例集
	public String delSet(){			
		service.del(setId);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//创建新的测试用例集或者修改
	public String editSet(){
		User user=(User) StrutsMaps.getSessionMap().get("user");
		if(caseSet.getSetId()==null){
			//新增
			caseSet.setUser(user);
			caseSet.setCreateTime(new Timestamp(System.currentTimeMillis()));
			caseSet.setLastModifyUser(user.getRealName());
			caseSet.setSetDesc(caseSet.getSetName());
			caseSet.setStatus("0");
			caseSet.setTestCount(0);
		}else{
			//修改
			WebCaseSet caseSet1=service.get(caseSet.getSetId());
			caseSet.setUser(caseSet1.getUser());
			caseSet.setCreateTime(caseSet1.getCreateTime());
			caseSet.setLastModifyUser(user.getRealName());
			caseSet.setTestCount(caseSet1.getTestCount());
		}			
		service.edit(caseSet);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	
	/*//测试集的测试
	public String batchTest(){
		User user=(User) StrutsMaps.getSessionMap().get("user");			
		WebCaseSet caseSet1=service.get(setId);
		Set<WebCaseSetComp> comps=caseSet1.getComps();
		WebConfig config=gService.findConfig(0);
		WebCase webCase=null;
		WebReportSet reportSet=new WebReportSet(new Timestamp(System.currentTimeMillis()),caseSet1);
		int ret=rService.addReportSet(reportSet);
		reportSet.setReportSetId(ret);
		for(WebCaseSetComp comp:comps){	
			if(comp.getStatus().equals("0")){
				webCase=comp.getWebCase();
				if(webCase.getSteps().size()<1){				
					continue;
				}
				WebReportCase reportCase=new WebReportCase(webCase,reportSet,new Timestamp(System.currentTimeMillis()));
				ret=rService.addReportCase(reportCase);
				reportCase.setReportCaseId(ret);
				ActionContext ac = ActionContext.getContext();     
		        ServletContext sc = (ServletContext) ac.get(ServletActionContext.SERVLET_CONTEXT);     
		        String path= sc.getRealPath("/");  
				List<WebReport> reports=TestCore.runTest(webCase,reportCase,path,config,user.getRealName());
				for(WebReport report:reports){
					rService.addReport(report);
				}
			}
		}
		service.addCount(setId);			
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}*/
	
	//供自动化定时任务调用的
	public String runTest(){
		User user=new User();
		user.setUserId(2);
		WebCaseSet caseSet1=service.get(setId);
		Set<WebCaseSetComp> comps=caseSet1.getComps();
		WebConfig config=gService.findConfig(0);
		WebCase webCase=null;
		WebReportSet reportSet=new WebReportSet(new Timestamp(System.currentTimeMillis()),caseSet1);
		int ret=rService.addReportSet(reportSet);
		reportSet.setReportSetId(ret);
		for(WebCaseSetComp comp:comps){	
			if(comp.getStatus().equals("0")){
				webCase=comp.getWebCase();
				if(webCase.getSteps().size()<1){				
					continue;
				}
				WebReportCase reportCase=new WebReportCase(webCase,reportSet,new Timestamp(System.currentTimeMillis()));
				ret=rService.addReportCase(reportCase);
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
			}
		}
		service.addCount(setId);			
		jsonMap.put("returnCode", 0);		
		return SUCCESS;
	}
	
	
	
	//获取当前用户的审核记录
	public String auditRecord(){
		User user=(User) StrutsMaps.getSessionMap().get("user");
		List<WebCaseSetComp> comps=cService.findByUser(user.getUserId());
		for(WebCaseSetComp comp:comps){
			comp.setCaseName();
			comp.setSetName();
		}
		jsonMap.put("returnCode", 0);
		jsonMap.put("data", comps);			
		return SUCCESS;
	}
	
	//更新状态关联表中的关联状态
	public String updateCompStatus(){
		cService.updateStatus(id, status);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	public String getSetCase(){
		List<WebCaseSetComp> comps=cService.findAll(setId);
		for(WebCaseSetComp comp:comps){
			comp.setUsername();
			comp.setCaseId();
			comp.setCaseName();
		}
		jsonMap.put("data", comps);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//获取set信息
	public String getSet(){			
		caseSet=service.get(setId);
		jsonMap.put("caseSet", caseSet);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	/////////////////////////////////GET-SET/////////////////////////////////////////////////////////
	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	
	public void setSetId(Integer setId) {
		this.setId = setId;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}
	
	@JSON(serialize=false)
	@Override
	public WebCaseSet getModel() {
		// TODO Auto-generated method stub
		return caseSet;
	}
	
}
