package com.dcits.action.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.dcits.bean.message.TestReport;
import com.dcits.bean.message.TestResult;
import com.dcits.bean.message.TestSet;
import com.dcits.service.message.ReportService;
import com.dcits.service.message.TestSetService;
import com.opensymphony.xwork2.ActionSupport;

public class ReportAction extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private ReportService reportService;
	@Autowired
	private TestSetService setService;
	
	private Integer reportId;
	
	private String mode;
	
	private Integer resultId;
	
	private Map<String,Object> jsonMap=new HashMap<String,Object>();
	
	//չʾ���еĲ��Ա���
	public String list(){
			List<TestReport> reports=reportService.findAll();
			for(TestReport tr:reports){
				tr.setCreateUserName();
				tr.setSceneNum();
				if(!tr.getTestMode().equals("0")){
					TestSet set = setService.findById(Integer.parseInt(tr.getTestMode()));
					if(set!=null){
						tr.setSetName(set.getSetName());
					}else{
						tr.setSetName("���Լ���ɾ��");
					}
					
				}
				
			}
			jsonMap.put("data", reports);
			jsonMap.put("returnCode", 0);				
		return SUCCESS;
	}
	
	//չʾָ���Ĳ�������result
	public String showResult(){
		List<TestResult> results=reportService.findResult(reportId, mode);
		jsonMap.put("data", results);
		jsonMap.put("returnCode", 0);			
		return SUCCESS;
	}
	
	//չʾ��������
	public String showResultDetail(){
		TestResult result=reportService.getResult(resultId);
		jsonMap.put("result", result);
		jsonMap.put("returnCode",0);	
		return SUCCESS;
	}
	
	//����html����
	public String htmlView(){
		TestReport report=reportService.getReport(reportId);
		Set<TestResult> results=report.getTrs();
		report.setSceneNum();
		jsonMap.put("report", report);
		jsonMap.put("results", results);
		jsonMap.put("returnCode",0);		
		return SUCCESS;
	}
	
	//ɾ������
	public String del(){
		reportService.delReport(reportId);
		jsonMap.put("returnCode",0);
		return SUCCESS;
	}
	/////////////////////////////////////////////GET-SET///////////////////////////////////////////////////////////////
	public void setResultId(Integer resultId) {
		this.resultId = resultId;
	}
	
	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}
	
	public void setMode(String mode) {
		this.mode = mode;
	}
	public Map<String, Object> getJsonMap() {
		return jsonMap;
	}
	
}
