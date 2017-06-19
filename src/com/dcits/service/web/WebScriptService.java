package com.dcits.service.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcits.bean.web.WebScriptInfo;
import com.dcits.bean.web.WebScriptReport;
import com.dcits.dao.web.WebScriptInfoDao;
import com.dcits.dao.web.WebScriptReportDao;

@Service
public class WebScriptService {
	@Autowired
	private WebScriptInfoDao infoDao;
	@Autowired
	private WebScriptReportDao reportDao;
	
	/**
	 * �������еĽű���Ϣ-�����
	 * @return
	 */
	public List<WebScriptInfo> findAllScripts(String type){
		return infoDao.findAll(type);
	}
	
	/**
	 * ����ָ��ID�Ľű���Ϣ
	 * @param scriptId
	 * @return
	 */
	public WebScriptInfo getScript(Integer scriptId){
		return infoDao.get(scriptId);
	}
	
	/**
	 * �༭���������ű���Ϣ
	 * @param script
	 */
	public void editScript(WebScriptInfo script){
		infoDao.edit(script);
	}
	
	/**
	 * ɾ���ű�
	 * @param scriptId
	 */
	public void delScript(Integer scriptId){
		infoDao.delete(scriptId);
	}
	
	/**
	 * �����µ�script���Ա���
	 * @param repoort
	 * @return
	 */
	public int saveReport(WebScriptReport repoort){
		return reportDao.save(repoort);
	}
	
	/**
	 * �����������һ������ʱ��
	 * @param id  �������еĳ����ű��͹����ű�
	 */
	public void updateRunTime(String[] id){
		for(String s:id){
			infoDao.updateRunTime(Integer.valueOf(s));
		}
	}
	
	/**
	 * �������в��Ա���
	 * @return
	 */
	public List<WebScriptReport> findAllReports(){
		return reportDao.findAll();
	}
	
	/**
	 * ɾ��ָ���Ĳ��Ա���
	 * @param reportId
	 */
	public void delReport(Integer reportId){
		reportDao.delete(reportId);
	}
	
	/**
	 * ��ȡָ���Ĳ��Ա���
	 * @param reportId
	 * @return
	 */
	public WebScriptReport getReport(Integer reportId){
		return reportDao.get(reportId);
	}
}
