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
	 * 查找所有的脚本信息-按类别
	 * @return
	 */
	public List<WebScriptInfo> findAllScripts(String type){
		return infoDao.findAll(type);
	}
	
	/**
	 * 查找指定ID的脚本信息
	 * @param scriptId
	 * @return
	 */
	public WebScriptInfo getScript(Integer scriptId){
		return infoDao.get(scriptId);
	}
	
	/**
	 * 编辑或者新增脚本信息
	 * @param script
	 */
	public void editScript(WebScriptInfo script){
		infoDao.edit(script);
	}
	
	/**
	 * 删除脚本
	 * @param scriptId
	 */
	public void delScript(Integer scriptId){
		infoDao.delete(scriptId);
	}
	
	/**
	 * 保存新的script测试报告
	 * @param repoort
	 * @return
	 */
	public int saveReport(WebScriptReport repoort){
		return reportDao.save(repoort);
	}
	
	/**
	 * 批量更新最后一次运行时间
	 * @param id  包括运行的场景脚本和公共脚本
	 */
	public void updateRunTime(String[] id){
		for(String s:id){
			infoDao.updateRunTime(Integer.valueOf(s));
		}
	}
	
	/**
	 * 查找所有测试报告
	 * @return
	 */
	public List<WebScriptReport> findAllReports(){
		return reportDao.findAll();
	}
	
	/**
	 * 删除指定的测试报告
	 * @param reportId
	 */
	public void delReport(Integer reportId){
		reportDao.delete(reportId);
	}
	
	/**
	 * 获取指定的测试报告
	 * @param reportId
	 * @return
	 */
	public WebScriptReport getReport(Integer reportId){
		return reportDao.get(reportId);
	}
}
