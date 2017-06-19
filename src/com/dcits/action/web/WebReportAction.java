package com.dcits.action.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dcits.bean.user.User;
import com.dcits.bean.web.WebReport;
import com.dcits.bean.web.WebReportCase;
import com.dcits.bean.web.WebReportSet;
import com.dcits.service.web.WebReportService;
import com.dcits.util.StrutsMaps;
import com.opensymphony.xwork2.ActionSupport;

@Controller
public class WebReportAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	@Autowired
	private WebReportService service;
	
	private Integer reportSetId;
	
	private Integer reportCaseId;
	
	//��ȡ���еĲ��Լ�����
	public String listReportSet(){
		List<WebReportSet> reportSets=service.findAllReportSet();
		for(WebReportSet s:reportSets){
			s.setTestDetails();
			s.setSetName();
		}
		jsonMap.put("data", reportSets);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//��ȡָ���Ĳ��Լ�����
	public String getReportSet(){
		WebReportSet reportSet=service.getReportSet(reportSetId);
		reportSet.setSetName();
		reportSet.setTestDetails();
		jsonMap.put("reportSet", reportSet);
		jsonMap.put("reportCases", reportSet.getWebReportCases());
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//ɾ��ָ��reportSet
	public String delReportSet(){
		service.delReportSet(reportSetId);
		jsonMap.put("returnCode", 0);
		return SUCCESS;
	}
	
	//չʾ���Խ���б�,ֻչʾ��ǰ�û��Ĳ�������������
	//���������reportSetId,�����ָ��reportSet�µ�
	public String listReportCase(){
		List<WebReportCase> reportCases=null;
		if(reportSetId!=null){
			reportCases=new ArrayList<WebReportCase>(service.getReportSet(reportSetId).getWebReportCases());
		}else{
			User user=(User)StrutsMaps.getSessionMap().get("user");
			reportCases=service.findAllReportCase(user.getUserId());
		}			
		for(WebReportCase w:reportCases){
			w.setStepNum();
			w.setStatus();
			w.setBrowser();
			w.setCaseName();
		}
		jsonMap.put("data", reportCases);
		jsonMap.put("returnCode", 0);	
		return SUCCESS;
	}
	
	//ɾ��reportCase
	public String delReportCase(){
		service.delReportCase(reportCaseId);
		jsonMap.put("returnCode", 0);
		
		return SUCCESS;
	}
	
	//��ȡ���Ա����µĲ��Բ��豨��
	public String stepReport(){
		WebReportCase rs=service.getReportCase(reportCaseId);
		List<WebReport> reports=new ArrayList<WebReport>();
		Set<WebReport> ss=rs.getReports();
		for(int i=1;i<=ss.size();i++){
			for(WebReport r:ss){
				if(r.getWebStep().getOrderNum()==i){
					r.setStepName();
					reports.add(r);
				}
			}
		}
		rs.setStepNum();
		rs.setStatus();
		rs.setBrowser();
		rs.setCaseName();
		jsonMap.put("reportInfo", rs);
		jsonMap.put("data", reports);
		jsonMap.put("returnCode", 0);		
		return SUCCESS;
	}
	
	///////////////////////////////////GET-SET//////////////////////////////////////////
	public void setReportCaseId(Integer reportCaseId) {
		this.reportCaseId = reportCaseId;
	}
	
	public void setReportSetId(Integer reportSetId) {
		this.reportSetId = reportSetId;
	}
	
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}

}
